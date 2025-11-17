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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CartCacheUserService {
    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;
    private final CartLuaRepository cartCacheRepository;

    private final CartUserService cartUserService;
    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final UserEntityFinder userEntityFinder;

    public void addCartMenu(CartAddMenuRequest request) {
        // 로그인 계정
        Long accountId = AuthAccountUtil.getAuthAccount().getAccountId();

        // 점포 메뉴 조회
        ShopMenuCacheResponse shopMenu = shopMenuEntityFinder.getShopMenuCacheResponseById(request.shopMenuId());

        // 판매하지 않는 메뉴 검사
        if (shopMenu.shopMenuStatus() == ShopMenuStatus.DEACTIVATE)
            throw new ShopMenuDeactivateException();

        long timeout = 3L;

        // lua script 사용
        Long result = cartCacheRepository.addCartMenu(
                accountId,
                shopMenu.shopId(),
                request.shopMenuId(),
                shopMenu.price(),
                timeout
        );

        // 캐시 성공 시
        if (result == 1L)
            // 캐시 저장 타이머
            cartCacheRepository.setSaveDBTimer(accountId, timeout);
        else
            // 레디스 장애 발생 시 DB저장
            cartUserService.addCartMenu(request);
    }

    @Transactional(readOnly = true)
    public CartResponse getCartResponse() {
        // 로그인 계정
        Long accountId = AuthAccountUtil.getAuthAccount().getAccountId();

        // 캐시 조회 실패시 Optional.empty 반환
        Optional<CartResponse> cartResponse = cartCacheRepository.findAllCartResponse(accountId);

        // 캐시 성공
        if (cartResponse.isPresent())
            return cartResponse.get();

        // 캐시 갱신
        long timeout = 3L;
        cartCacheRepository.saveCartToCache(accountId, timeout);

        // 캐시 실패 시 DB 반환
        return cartUserService.getCartResponse();
    }

    @Transactional
    public CartResponse updateCartMenu(CartUpdateMenuRequest request) {
        // 로그인 계정
        Long accountId = AuthAccountUtil.getAuthAccount().getAccountId();

        long timeout = 3L;

        boolean result = cartCacheRepository.updateCartMenuQuantity(
                accountId,
                request.shopMenuId(),
                request.quantity(),
                timeout
        );

        // 캐시 성공
        if (result) {
            // 캐시 저장 타이머
            cartCacheRepository.setSaveDBTimer(accountId, timeout);

            return getCartResponse();
        }

        // 캐시 실패
        CartResponse cartResponse = cartUserService.updateCartMenu(request);

        // 캐시 갱신
        cartCacheRepository.saveCartToCache(accountId, 3L);

        return cartResponse;
    }

    @Transactional
    public CartResponse deleteCartMenu(CartDeleteMenuRequest request) {
        // 로그인 계정
        Long accountId = AuthAccountUtil.getAuthAccount().getAccountId();

        long timeout = 3L;

        boolean result = cartCacheRepository.deleteCartMenuQuantity(
                accountId,
                request.shopMenuId(),
                3L
        );
        // 캐시 성공
        if (result) {
            // 캐시 저장 타이머
            cartCacheRepository.setSaveDBTimer(accountId, timeout);

            return getCartResponse();
        }

        // 캐시 실패
        CartResponse cartResponse = cartUserService.deleteCartMenu(request);

        // 캐시 갱신
        cartCacheRepository.saveCartToCache(accountId, 3L);

        return cartResponse;
    }

    @Transactional
    public List<CartMenu> saveAllCache(Long accountId) {

        Optional<Map<String, Object>> optionalAllEntries = cartCacheRepository.getAllEntriesById(accountId);

        if (optionalAllEntries.isEmpty())
            return new ArrayList<>();

        Map<String, Object> allEntries = optionalAllEntries.get();

        User user = userEntityFinder.getUserByAccountId(accountId);

        Integer totalPriceInt = (Integer) allEntries.get(CartLuaRepository.TOTAL_PRICE_PREFIX);

        Cart cart = cartRepository.findById(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.of(user, totalPriceInt)));

        cart.updateTotalPrice(totalPriceInt);

        List<CartMenu> cartMenuList = allEntries.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(CartLuaRepository.MENU_KEY_PREFIX))
                .map(entry -> {
                    String menuKey = entry.getKey().substring(CartLuaRepository.MENU_KEY_PREFIX.length());
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
        return cartMenuRepository.saveAll(cartMenuList);
    }

    public void deleteSaveDBTimer(Long accountId) {
        cartCacheRepository.deleteSaveDBTimer(accountId);
    }
}
