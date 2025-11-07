package org.example.luckyburger.domain.cart.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.repository.CartCacheRepository;
import org.example.luckyburger.domain.cart.repository.CartLuaRepository;
import org.example.luckyburger.domain.cart.repository.CartMenuRepository;
import org.example.luckyburger.domain.cart.repository.CartRepository;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.exception.ShopMenuDeactivateException;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CartCacheUserService {
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;
    private final CartCacheRepository cartCacheRepository;
    private final CartLuaRepository cartLuaRepository;

    private final CartUserService cartUserService;
    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final UserEntityFinder userEntityFinder;

    public void addCartMenu(CartAddMenuRequest request) {
        // 로그인 유저
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        // 점포 메뉴 조회
        ShopMenuCacheResponse shopMenu = shopMenuEntityFinder.getShopMenuCacheResponseById(request.shopMenuId());

        // 판매하지 않는 메뉴 검사
        if (shopMenu.shopMenuStatus() == ShopMenuStatus.DEACTIVATE)
            throw new ShopMenuDeactivateException();

        long timeout = 3L;

        // lua script 사용
        cartLuaRepository.addCartMenu(
                userId,
                shopMenu.shopId(),
                request.shopMenuId(),
                shopMenu.price(),
                timeout
        );

        // 캐시 저장 타이머
        cartLuaRepository.setSaveDBTimer(userId, timeout);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartResponse() {
        // 로그인 유저
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        // 캐시 조회 실패시 Optional.empty 반환
        Optional<CartResponse> cartResponse = cartCacheRepository.findAllCartResponse(userId);

        long timeout = 3L;

        // 캐시 성공
        if (cartResponse.isPresent())
            return cartResponse.get();

        // 캐시 갱신
        cartLuaRepository.saveCartToCache(userId, timeout);

        // 캐시 실패 시 DB 반환
        return cartUserService.getCartResponse();
    }

    @Transactional
    public CartResponse updateCartMenu(CartUpdateMenuRequest request) {
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        long timeout = 3L;

        boolean result = cartLuaRepository.updateCartMenuQuantity(
                userId,
                request.shopMenuId(),
                request.quantity(),
                timeout
        );

        // 캐시 성공
        if (result) {
            // 캐시 저장 타이머
            cartLuaRepository.setSaveDBTimer(userId, timeout);

            return getCartResponse();
        }

        // 캐시 실패
        CartResponse cartResponse = cartUserService.updateCartMenu(request);

        // 캐시 갱신
        cartLuaRepository.saveCartToCache(userId, 3L);

        return cartResponse;
    }

    @Transactional
    public CartResponse deleteCartMenu(CartDeleteMenuRequest request) {
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        long timeout = 3L;

        boolean result = cartLuaRepository.deleteCartMenuQuantity(
                userId,
                request.shopMenuId(),
                3L
        );
        // 캐시 성공
        if (result) {
            // 캐시 저장 타이머
            cartLuaRepository.setSaveDBTimer(userId, timeout);

            return getCartResponse();
        }

        // 캐시 실패
        CartResponse cartResponse = cartUserService.deleteCartMenu(request);

        // 캐시 갱신
        cartLuaRepository.saveCartToCache(userId, 3L);

        return cartResponse;
    }

    @Transactional
    public void saveAllCache(Long accountId) {
        Map<String, Object> allEntries = cartCacheRepository.getAllEntries(accountId);

        User user = userEntityFinder.getUserByAccountId(accountId);

        Integer totalPriceInt = (Integer) allEntries.get(CartCacheRepository.TOTAL_PRICE_PREFIX);

        Cart cart = cartRepository.findById(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.of(user, totalPriceInt)));

        cart.updateTotalPrice(totalPriceInt);

        List<CartMenu> cartMenuList = allEntries.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(CartCacheRepository.MENU_KEY_PREFIX))
                .map(entry -> {
                    String menuKey = entry.getKey().substring(CartCacheRepository.MENU_KEY_PREFIX.length());
                    Long shopMenuId = Long.valueOf(menuKey);
                    Integer quantity = (Integer) entry.getValue();
                    ShopMenu shopMenu = shopMenuEntityFinder.getShopMenuById(shopMenuId);

                    return CartMenu.of(
                            cart,
                            shopMenu,
                            quantity
                    );
                })
                .toList();

        // 데이터 제거
        cartMenuRepository.deleteAllByCartId(cart.getId());
        // DB에 저장
        cartMenuRepository.saveAll(cartMenuList);
    }
}
