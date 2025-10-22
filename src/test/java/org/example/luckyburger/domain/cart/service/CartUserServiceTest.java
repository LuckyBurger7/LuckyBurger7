package org.example.luckyburger.domain.cart.service;

import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.exception.CartMenuBadRequestException;
import org.example.luckyburger.domain.cart.repository.CartRepository;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartUserServiceTest {

    @InjectMocks
    private CartUserService cartUserService;

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartEntityFinder cartEntityFinder;
    @Mock
    private CartMenuEntityFinder cartMenuEntityFinder;
    @Mock
    private ShopMenuEntityFinder shopMenuEntityFinder;
    @Mock
    private UserEntityFinder userEntityFinder;
    @Mock
    private AccountEntityFinder accountEntityFinder;
    @Mock
    private CartMenuService cartMenuService;

    private User user;
    private Cart cart;
    private ShopMenu shopMenu1;
    private ShopMenu shopMenu2;
    private ShopMenu shopMenu3;

    @BeforeEach
    void setUp() {
        // 인증
        AuthAccount principal = new AuthAccount(1L, "test@example.com", AccountRole.ROLE_USER);
        Authentication auth = new TestingAuthenticationToken(
                principal, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);

        Account account = Account.of("user@test.com", "홍길동", "password1!", AccountRole.ROLE_USER);

        // user & cart
        user = User.of(account, "010-1123-4456", "주소", "상세주소");
        cart = Cart.of(user, 0);
        ReflectionTestUtils.setField(cart, "id", 1L);

        when(accountEntityFinder.getAccountById(1L)).thenReturn(account);
        when(userEntityFinder.getUserByAccount(account)).thenReturn(user);

        // shop
        Shop shop1 = Shop.of("럭키버거 OO점", BusinessStatus.OPEN, "주소", "상세주소");
        Shop shop2 = Shop.of("럭키버거 OO점", BusinessStatus.OPEN, "주소", "상세주소");
        ReflectionTestUtils.setField(shop1, "id", 1L);
        ReflectionTestUtils.setField(shop2, "id", 2L);

        // menu
        Menu menu1 = Menu.of("치즈버거", MenuCategory.HAMBURGER, 5500);
        Menu menu2 = Menu.of("콜라", MenuCategory.DRINK, 2000);
        ReflectionTestUtils.setField(menu1, "id", 1L);
        ReflectionTestUtils.setField(menu2, "id", 2L);

        // shopMenu
        shopMenu1 = ShopMenu.of(shop1, menu1, ShopMenuStatus.ON_SALE, 0);
        shopMenu2 = ShopMenu.of(shop1, menu2, ShopMenuStatus.ON_SALE, 0);
        shopMenu3 = ShopMenu.of(shop2, menu1, ShopMenuStatus.ON_SALE, 0);
        ReflectionTestUtils.setField(shopMenu1, "id", 1L);
        ReflectionTestUtils.setField(shopMenu2, "id", 2L);
        ReflectionTestUtils.setField(shopMenu3, "id", 3L);
    }

    @Test
    void 유저의_장바구니가_없으면_새로_생성하고_메뉴를_추가한다() {
        // given
        CartAddMenuRequest request = new CartAddMenuRequest(1L);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);
        when(shopMenuEntityFinder.getShopMenuById(1L)).thenReturn(shopMenu1);

        when(cartMenuService.calculateTotalPrice(anyList())).thenAnswer(invocation -> {
            List<CartMenu> menus = invocation.getArgument(0);
            return menus.stream()
                    .mapToLong(cm -> cm.getShopMenu().getMenu().getPrice() * cm.getQuantity())
                    .sum();
        });

        // when
        cartUserService.addCartMenu(request);

        // then
        verify(cartRepository).save(any(Cart.class));
        assertEquals(5500L, cart.getTotalPrice());
    }

    @Test
    void 장바구니에_동일한_메뉴가_존재해_수량이_증가한다() {
        // given
        CartAddMenuRequest request = new CartAddMenuRequest(1L);
        CartMenu cartMenu = CartMenu.of(cart, shopMenu1, 1);
        List<CartMenu> cartMenus = new ArrayList<>();
        cartMenus.add(cartMenu);

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(shopMenuEntityFinder.getShopMenuById(1L)).thenReturn(shopMenu1);
        when(cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId())).thenReturn(cartMenus);

        when(cartMenuService.calculateTotalPrice(anyList())).thenAnswer(invocation -> {
            List<CartMenu> menus = invocation.getArgument(0);
            return menus.stream()
                    .mapToLong(cm -> cm.getShopMenu().getMenu().getPrice() * cm.getQuantity())
                    .sum();
        });

        // when
        cartUserService.addCartMenu(request);

        // then
        assertEquals(2, cartMenu.getQuantity());
        assertEquals(11000L, cart.getTotalPrice());
    }

    @Test
    void 장바구니에_있는_메뉴와_다른_가게의_메뉴를_담아_오류가_발생한다() {
        // given
        CartAddMenuRequest request = new CartAddMenuRequest(3L);
        List<CartMenu> cartMenus = List.of(CartMenu.of(cart, shopMenu1, 1));

        when(cartRepository.findByUserId(user.getId())).thenReturn(Optional.of(cart));
        when(shopMenuEntityFinder.getShopMenuById(3L)).thenReturn(shopMenu3);
        when(cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId())).thenReturn(cartMenus);

        // then
        assertThrows(CartMenuBadRequestException.class, () -> cartUserService.addCartMenu(request));
    }

    @Test
    void 장바구니_메뉴_수정에_성공한다() {
        // given
        CartMenu cartMenu1 = CartMenu.of(cart, shopMenu1, 1);
        CartMenu cartMenu2 = CartMenu.of(cart, shopMenu2, 3);
        List<CartMenu> cartMenus = new ArrayList<>();
        cartMenus.add(cartMenu1);
        cartMenus.add(cartMenu2);

        when(cartEntityFinder.getCartByUserId(user.getId())).thenReturn(cart);
        when(cartMenuEntityFinder.getCartMenuById(cartMenu1.getId())).thenReturn(cartMenu1);
        when(cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId())).thenReturn(cartMenus);
        when(cartMenuService.calculateTotalPrice(anyList())).thenAnswer(invocation -> {
            List<CartMenu> menus = invocation.getArgument(0);
            return menus.stream()
                    .mapToLong(cm -> cm.getShopMenu().getMenu().getPrice() * cm.getQuantity())
                    .sum();
        });

        // when
        CartUpdateMenuRequest request = new CartUpdateMenuRequest(cartMenu1.getId(), 3);
        CartResponse response = cartUserService.updateCartMenu(request);

        // then
        assertEquals(3, cartMenus.get(0).getQuantity());
        assertEquals(22500L, response.totalPrice());
    }

    @Test
    void 장바구니에_메뉴가_없어_메뉴_수정에_실패한다() {
        // given
        when(cartEntityFinder.getCartByUserId(user.getId())).thenReturn(cart);
        when(cartMenuEntityFinder.getCartMenuById(1L)).thenThrow(CartMenuBadRequestException.class);

        // when & then
        CartUpdateMenuRequest request = new CartUpdateMenuRequest(1L, 2);
        assertThrows(CartMenuBadRequestException.class, () -> cartUserService.updateCartMenu(request));
    }

    @Test
    void 장바구니_메뉴_삭제에_성공한다() {
        // given
        CartMenu cartMenu1 = CartMenu.of(cart, shopMenu1, 3);
        List<CartMenu> cartMenus = new ArrayList<>();
        cartMenus.add(cartMenu1);

        when(cartEntityFinder.getCartByUserId(user.getId())).thenReturn(cart);
        when(cartMenuEntityFinder.getCartMenuById(cartMenu1.getId())).thenReturn(cartMenu1);

        when(cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId()))
                .thenAnswer(invocation -> {
                    cartMenus.remove(cartMenu1); // 삭제된 메뉴 제거
                    return cartMenus;
                });

        when(cartMenuService.calculateTotalPrice(anyList())).thenAnswer(invocation -> {
            List<CartMenu> menus = invocation.getArgument(0);
            return menus.stream()
                    .mapToLong(cm -> cm.getShopMenu().getMenu().getPrice() * cm.getQuantity())
                    .sum();
        });

        // when
        CartDeleteMenuRequest request = new CartDeleteMenuRequest(cartMenu1.getId());
        CartResponse response = cartUserService.deleteCartMenu(request);

        // then
        assertTrue(response.cartMenus().isEmpty());
        assertEquals(0L, response.totalPrice());
    }

    @Test
    void 장바구니에_메뉴가_없어_메뉴_삭제에_실패한다() {
        when(cartEntityFinder.getCartByUserId(user.getId())).thenReturn(cart);
        when(cartMenuEntityFinder.getCartMenuById(1L)).thenThrow(CartMenuBadRequestException.class);

        // when & then
        CartDeleteMenuRequest request = new CartDeleteMenuRequest(1L);
        assertThrows(CartMenuBadRequestException.class, () -> cartUserService.deleteCartMenu(request));
    }
}
