package org.example.luckyburger.domain.cart.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.repository.CartCacheRepository;
import org.example.luckyburger.domain.cart.repository.CartLuaRepository;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.exception.ShopMenuDeactivateException;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CartCacheUserService {
    private final CartCacheRepository cartCacheRepository;
    private final CartLuaRepository cartLuaRepository;

    private final CartUserService cartUserService;
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;
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

        // lua script 사용
        cartLuaRepository.addCartMenu(
                userId,
                shopMenu.shopId(),
                request.shopMenuId(),
                shopMenu.price(),
                3L
        );
    }

    @Transactional(readOnly = true)
    public CartResponse getCartResponse() {
        // 로그인 유저
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        Optional<CartResponse> cartResponse = cartCacheRepository.findAllCartResponse(userId);

        // 캐시 실패 시 DB 반환
        return cartResponse.orElseGet(cartUserService::getCartResponse);
    }

    @Transactional
    public CartResponse updateCartMenu(CartUpdateMenuRequest request) {
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        boolean result = cartLuaRepository.updateCartMenuQuantity(
                userId,
                request.shopMenuId(),
                request.quantity(),
                3L
        );

        // 캐시 성공
        if (result)
            return getCartResponse();

        CartResponse cartResponse = cartUserService.updateCartMenu(request);

        // 갱신


        return cartResponse;
    }

    @Transactional
    public CartResponse deleteCartMenu(CartDeleteMenuRequest request) {
        Long userId = AuthAccountUtil.getAuthAccount().getAccountId();

        boolean result = cartLuaRepository.deleteCartMenuQuantity(
                userId,
                request.shopMenuId(),
                3L
        );
        // 캐시 성공
        if (result)
            return getCartResponse();

        return cartUserService.deleteCartMenu(request);
    }
}
