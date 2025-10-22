package org.example.luckyburger.domain.cart.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.exception.CartMenuBadRequestException;
import org.example.luckyburger.domain.cart.exception.CartMenuForbiddenException;
import org.example.luckyburger.domain.cart.repository.CartMenuRepository;
import org.example.luckyburger.domain.cart.repository.CartRepository;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CartUserService {

    private final CartRepository cartRepository;
    private final CartMenuRepository cartMenuRepository;
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;
    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final UserEntityFinder userEntityFinder;
    private final AccountEntityFinder accountEntityFinder;
    private final CartMenuService cartMenuService;

    @Transactional
    public void addCartMenu(CartAddMenuRequest request) {
        User user = findUser();

        // 장바구니가 없다면 생성 및 save
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> cartRepository.save(Cart.of(user, 0)));

        // shopMenu 및 cartMenus 조회
        ShopMenu shopMenu = shopMenuEntityFinder.getShopMenu(request.shopMenuId());
        List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId());

        CartMenu sameCartMenu = null;
        for (CartMenu menu : cartMenus) {
            // 장바구니에 있는 메뉴와 다른 가게의 메뉴를 담으려고 했다면 BAD_REQUEST
            if (!menu.getShopMenu().getShop().getId().equals(shopMenu.getShop().getId())) {
                throw new CartMenuBadRequestException();
            }
            // cartMenu의 shopMenuId 와 shopMenu의 Id가 같은 메뉴가 존재하면 CartMenu를 반환
            if (menu.getShopMenu().getId().equals(shopMenu.getId())) {
                sameCartMenu = menu;
            }
        }

        // 같은메뉴가 있다면 현재 수량 + 1
        // 없다면 메뉴 추가 및 리스트에 추가
        if (sameCartMenu != null) {
            sameCartMenu.updateQuantity(sameCartMenu.getQuantity() + 1);
        } else {
            CartMenu cartMenu = CartMenu.of(cart, shopMenu, 1);
            cartMenuRepository.save(cartMenu);
            cartMenus.add(cartMenu);
        }

        // 리스트를 토대로 총합 금액 계산
        cart.updateTotalPrice(cartMenuService.calculateTotalPrice(cartMenus));
    }

    @Transactional(readOnly = true)
    public CartResponse getCartResponse() {
        User user = findUser();
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        return CartResponse.of(cart, cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId()));
    }

    @Transactional
    public CartResponse updateCartMenu(CartUpdateMenuRequest request) {
        User user = findUser();
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        // 해당하는 cartMenu를 찾아서 반환
        CartMenu cartMenu = findCartMenu(cart.getId(), request.cartMenuId());

        // 해당 cartMenu의 수량 수정
        cartMenu.updateQuantity(request.quantity());

        // 리스트 조회 후 총합 금액 계산
        List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId());
        cart.updateTotalPrice(cartMenuService.calculateTotalPrice(cartMenus));

        return CartResponse.of(cart, cartMenus);
    }

    @Transactional
    public CartResponse deleteCartMenu(CartDeleteMenuRequest request) {
        User user = findUser();
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        // 해당하는 cartMenu를 찾아서 반환
        CartMenu cartMenu = findCartMenu(cart.getId(), request.cartMenuId());

        // 해당 cartMenu 삭제
        cartMenuService.deleteCartMenu(cartMenu);

        // 리스트 조회 후 총합 금액 계산
        List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId());
        cart.updateTotalPrice(cartMenuService.calculateTotalPrice(cartMenus));

        return CartResponse.of(cart, cartMenus);
    }


    //===== 헬퍼 메서드 =====

    // 로그인 중인 유저의 정보를 가져오는 메서드
    private User findUser() {
        Account account = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        return userEntityFinder.getUserByAccount(account);
    }

    // 해당 메뉴가 있는지 체크 후 CartMenu 반환
    // 메뉴가 없으면 NOT_FOUND, CartMenu의 cartId가 user의 cartId와 다르면 FORBIDDEN
    private CartMenu findCartMenu(Long cartId, Long cartMenuId) {
        CartMenu cartMenu = cartMenuEntityFinder.getCartMenuById(cartMenuId);

        if (!cartMenu.getCart().getId().equals(cartId)) {
            throw new CartMenuForbiddenException();
        }

        return cartMenu;
    }
}
