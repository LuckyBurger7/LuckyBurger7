package org.example.luckyburger.domain.order.service;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.cart.service.CartService;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.order.code.OrderErrorCode;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderPrepareResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.entity.OrderForm;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.*;
import org.example.luckyburger.domain.order.repository.OrderFormRepository;
import org.example.luckyburger.domain.order.repository.OrderMenuRepository;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.example.luckyburger.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderUserServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMenuRepository orderMenuRepository;
    @Mock
    private OrderFormRepository orderFormRepository;
    @Mock
    private UserEntityFinder userEntityFinder;
    @Mock
    private AccountEntityFinder accountEntityFinder;
    @Mock
    private OrderEntityFinder orderEntityFinder;
    @Mock
    private OrderMenuEntityFinder orderMenuEntityFinder;
    @Mock
    private OrderFormEntityFinder orderFormEntityFinder;
    @Mock
    private UserService userService;
    @Mock
    private ShopEntityFinder shopEntityFinder;
    @Mock
    private CartService cartService;
    @Mock
    private CartEntityFinder cartEntityFinder;
    @Mock
    private CartMenuEntityFinder cartMenuEntityFinder;
    @InjectMocks
    private OrderUserService orderUserService;

    private Account account;
    private User user;
    private Shop shop;
    private Cart cart;

    @BeforeEach
    void setUp() {
        AuthAccount principal = new AuthAccount(1L, "test@example.com", AccountRole.ROLE_USER);
        Authentication auth = new TestingAuthenticationToken(
                principal, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        account = Account.of("user@test.com", "홍길동", "password1!", AccountRole.ROLE_USER);
        user = User.of(account, "010-0000-0000", "서울 강남구 oo", "oo아파트 101동 1001호");
        shop = Shop.of("럭키버거 강남점", BusinessStatus.OPEN, "서울 강남구", "상세 주소");
        cart = Cart.of(user, 0);

        ReflectionTestUtils.setField(user, "id", 101L);
        ReflectionTestUtils.setField(shop, "id", 201L);
        ReflectionTestUtils.setField(cart, "id", 301L);

        when(accountEntityFinder.getAccountById(1L)).thenReturn(account);
        when(userEntityFinder.getUserByAccount(account)).thenReturn(user);
    }

    @Test
    void 주문_준비에_성공한다() {
        //given
        ReflectionTestUtils.setField(shop, "status", BusinessStatus.OPEN);
        ReflectionTestUtils.setField(user, "point", 10000);
        Menu menu = Menu.of("치즈버거", MenuCategory.HAMBURGER, 10000);
        ShopMenu shopMenu = ShopMenu.of(shop, menu, ShopMenuStatus.ON_SALE, 0);
        CartMenu cartMenu = CartMenu.of(cart, shopMenu, 2);
        ReflectionTestUtils.setField(cart, "totalPrice", 20000);
        when(cartMenuEntityFinder.getByCartId(cart.getId())).thenReturn(List.of(cartMenu));
        when(shopEntityFinder.getShopById(any())).thenReturn(shop);
        when(cartEntityFinder.getCartByUserId(any())).thenReturn(cart);

        //when
        OrderPrepareResponse resp = orderUserService.prepareOrderResponse();

        //then
        assertThat(resp.shopName()).isEqualTo("럭키버거 강남점");
        assertThat(resp.receiver()).isEqualTo("홍길동");
        assertThat(resp.phone()).isEqualTo("010-0000-0000");
        assertThat(resp.address()).isEqualTo("서울 강남구 oo");
        assertThat(resp.street()).isEqualTo("oo아파트 101동 1001호");
        assertThat(resp.point()).isEqualTo(10000);
        assertThat(resp.totalPrice()).isEqualTo(20000);

        assertThat(resp.items()).hasSize(1);
        assertThat(resp.items().get(0).name()).isEqualTo("치즈버거");
        assertThat(resp.items().get(0).unitPrice()).isEqualTo(10000);
        assertThat(resp.items().get(0).quantity()).isEqualTo(2);

        verify(accountEntityFinder).getAccountById(1L);
        verify(userEntityFinder).getUserByAccount(account);
        verify(cartEntityFinder).getCartByUserId(101L);
        verify(cartMenuEntityFinder).getByCartId(301L);
        verify(shopEntityFinder).getShopById(201L);
    }

    @Test
    void 주문_준비_실패_매장_영업종료() {
        //given
        ReflectionTestUtils.setField(user, "point", 10000);
        Menu menu = Menu.of("치즈버거", MenuCategory.HAMBURGER, 10000);
        ShopMenu shopMenu = ShopMenu.of(shop, menu, ShopMenuStatus.ON_SALE, 0);
        CartMenu cartMenu = CartMenu.of(cart, shopMenu, 2);
        ReflectionTestUtils.setField(cart, "totalPrice", 20000);
        when(cartMenuEntityFinder.getByCartId(cart.getId())).thenReturn(List.of(cartMenu));
        when(shopEntityFinder.getShopById(any())).thenReturn(shop);
        when(cartEntityFinder.getCartByUserId(any())).thenReturn(cart);

        ReflectionTestUtils.setField(shop, "status", BusinessStatus.CLOSED);

        // expect
        assertThatThrownBy(() -> orderUserService.prepareOrderResponse())
                .isInstanceOf(ShopNotOpenedException.class)
                .extracting(e -> ((GlobalException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.SHOP_NOT_OPENED);
    }

    @Test
    void 주문_준비_실패_장바구니_비어있음() {
        //given
        ReflectionTestUtils.setField(shop, "status", BusinessStatus.OPEN);
        ReflectionTestUtils.setField(user, "point", 10000);
        when(cartMenuEntityFinder.getByCartId(cart.getId())).thenReturn(List.of());
        when(cartEntityFinder.getCartByUserId(any())).thenReturn(cart);

        // expect
        assertThatThrownBy(() -> orderUserService.prepareOrderResponse())
                .isInstanceOf(EmptyOrderException.class)
                .extracting(e -> ((GlobalException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.EMPTY_ORDER);
    }

    @Test
    void 주문_생성에_성공한다() {
        //given
        ReflectionTestUtils.setField(shop, "status", BusinessStatus.OPEN);
        ReflectionTestUtils.setField(user, "point", 10000);
        Menu menu = Menu.of("치즈버거", MenuCategory.HAMBURGER, 10000);
        ShopMenu shopMenu = ShopMenu.of(shop, menu, ShopMenuStatus.ON_SALE, 0);
        OrderForm orderForm = OrderForm.of(user, shopMenu, 2);
        when(shopEntityFinder.getShopById(any())).thenReturn(shop);
        when(cartEntityFinder.getCartByUserId(any())).thenReturn(cart);
        when(orderFormEntityFinder.getAllOrderFormByUser(any(User.class))).thenReturn(List.of(orderForm));


        var request = new OrderCreateRequest(
                201L,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000
        );

        //when
        OrderResponse response = orderUserService.createOrderResponse(request);

        //then
        assertThat(response.amount().subtotal()).isEqualTo(20000);
        assertThat(response.amount().pay()).isEqualTo(15000);
        assertThat(response.status()).isEqualTo(OrderStatus.WAITING);

        verify(orderRepository).save(any(Order.class));
        verify(orderMenuRepository).save(any(OrderMenu.class));
        verify(cartService).clear(any());
        verify(userService).deductPoints(anyLong(), eq(5000));
    }

    @Test
    void 주문_실패_매장_영업_종료() {
        // given
        ReflectionTestUtils.setField(user, "point", 10000);
        Menu menu = Menu.of("치즈버거", MenuCategory.HAMBURGER, 10000);
        ShopMenu shopMenu = ShopMenu.of(shop, menu, ShopMenuStatus.ON_SALE, 0);
        ReflectionTestUtils.setField(cart, "totalPrice", 20000);
        OrderForm orderForm = OrderForm.of(user, shopMenu, 2);
        when(shopEntityFinder.getShopById(any())).thenReturn(shop);
        when(orderFormEntityFinder.getAllOrderFormByUser(any(User.class))).thenReturn(List.of(orderForm));

        ReflectionTestUtils.setField(shop, "status", BusinessStatus.CLOSED);

        var request = new OrderCreateRequest(
                201L,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000
        );

        // expect
        assertThatThrownBy(() -> orderUserService.createOrderResponse(request))
                .isInstanceOf(ShopNotOpenedException.class)
                .extracting(e -> ((GlobalException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.SHOP_NOT_OPENED);
    }

    @Test
    void 주문_실패_포인트_초과_사용() {
        // given
        ReflectionTestUtils.setField(shop, "status", BusinessStatus.OPEN);
        Menu menu = Menu.of("치즈버거", MenuCategory.HAMBURGER, 10000);
        ShopMenu shopMenu = ShopMenu.of(shop, menu, ShopMenuStatus.ON_SALE, 0);
        ReflectionTestUtils.setField(cart, "totalPrice", 20000);
        OrderForm orderForm = OrderForm.of(user, shopMenu, 2);
        when(shopEntityFinder.getShopById(any())).thenReturn(shop);
        when(orderFormEntityFinder.getAllOrderFormByUser(any(User.class))).thenReturn(List.of(orderForm));

        ReflectionTestUtils.setField(user, "point", 0);

        var request = new OrderCreateRequest(
                201L,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000
        );

        // expect
        assertThatThrownBy(() -> orderUserService.createOrderResponse(request))
                .isInstanceOf(PointExceedBalanceException.class)
                .extracting(e -> ((GlobalException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.POINT_EXCEEDS_BALANCE);
    }

    @Test
    void 주문_단일조회_성공() {
        // given
        Order order = Order.of(
                shop,
                user,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000,
                23000,
                18000,
                LocalDateTime.now(),
                OrderStatus.COOKING
        );
        ReflectionTestUtils.setField(order, "id", 101L);
        when(orderEntityFinder.getOrderById(101L)).thenReturn(order);

        Menu menu1 = Menu.of("치즈버거", MenuCategory.HAMBURGER, 7000);
        Menu menu2 = Menu.of("감자튀김", MenuCategory.SIDE, 4000);
        ShopMenu shopMenu1 = ShopMenu.of(shop, menu1, ShopMenuStatus.ON_SALE, 0);
        ShopMenu shopMenu2 = ShopMenu.of(shop, menu2, ShopMenuStatus.ON_SALE, 0);

        OrderMenu om1 = OrderMenu.of(order, shopMenu1, 2);
        OrderMenu om2 = OrderMenu.of(order, shopMenu2, 1);

        when(orderMenuEntityFinder.getAllOrderMenuByOrderId(101L))
                .thenReturn(List.of(om1, om2));

        //when
        OrderResponse response = orderUserService.getOrderResponse(101L);

        //then
        assertThat(response.orderId()).isEqualTo(101L);
        assertThat(response.receiver()).isEqualTo("홍길동");
        assertThat(response.items()).hasSize(2);
        assertThat(response.amount().subtotal()).isEqualTo(23000);
        assertThat(response.status()).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    void 주문_단일조회_실패_권한없음() {
        // given
        User otherUser = User.of(account, "010-1111-2222", "서울", "주소");
        ReflectionTestUtils.setField(otherUser, "id", 99L);

        Order order = Order.of(
                shop,
                otherUser,
                "다른사람",
                "010-1111-2222",
                "서울시",
                "주소",
                "요청사항",
                null,
                0,
                10000,
                10000,
                LocalDateTime.now(),
                OrderStatus.WAITING
        );
        ReflectionTestUtils.setField(order, "id", 777L);

        when(orderEntityFinder.getOrderById(777L)).thenReturn(order);

        // when & then
        assertThatThrownBy(() -> orderUserService.getOrderResponse(777L))
                .isInstanceOf(UnauthorizedOrderAccessException.class)
                .extracting(e -> ((GlobalException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.UNAUTHORIZED_ORDER_ACCESS);
    }

    @Test
    void 주문_전체조회_성공() {
        // given
        Pageable pageable = PageRequest.of(0, 2);

        Order o1 = Order.of(shop, user, "홍길동", "010-0000-0000", "서울", "상세", "", null, 0, 23000, 20000, LocalDateTime.now(), OrderStatus.WAITING);
        Order o2 = Order.of(shop, user, "홍길동", "010-0000-0000", "서울", "상세", "", null, 0, 15000, 12000, LocalDateTime.now(), OrderStatus.COOKING);
        ReflectionTestUtils.setField(o1, "id", 101L);
        ReflectionTestUtils.setField(o2, "id", 102L);

        List<Order> orders = List.of(o1, o2);
        Page<Order> orderPage = new PageImpl<>(orders, pageable, 2);

        when(orderEntityFinder.getAllOrderByUserId(anyLong(), any(Pageable.class))).thenReturn(orderPage);

        Menu m = Menu.of("치즈버거", MenuCategory.HAMBURGER, 7000);
        ShopMenu sm = ShopMenu.of(shop, m, ShopMenuStatus.ON_SALE, 0);
        OrderMenu om1 = OrderMenu.of(o1, sm, 2);
        OrderMenu om2 = OrderMenu.of(o2, sm, 1);

        when(orderMenuEntityFinder.getAllOrderMenuByOrderIdIn(orders))
                .thenReturn(List.of(om1, om2));

        // when
        Page<OrderResponse> page = orderUserService.getAllOrderResponse(pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).items()).isNotEmpty();
    }

    @Test
    void 주문_전체조회_성공_비어있음() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Order> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(orderEntityFinder.getAllOrderByUserId(anyLong(), any(Pageable.class))).thenReturn(emptyPage);

        Page<OrderResponse> result = orderUserService.getAllOrderResponse(pageable);

        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void 주문_취소_성공_WAITING() {
        // given
        Order order = Order.of(
                shop,
                user,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000,
                23000,
                18000,
                LocalDateTime.now(),
                OrderStatus.WAITING
        );
        ReflectionTestUtils.setField(order, "id", 101L);
        when(orderEntityFinder.getOrderById(101L)).thenReturn(order);


        // when
        orderUserService.cancelOrder(101L);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        // 사용 포인트 환원 호출 확인
        Integer usedPoint = order.getPoint();
        if (usedPoint != null && usedPoint > 0) {
            verify(userService).addPoints(eq(user.getId()), eq(usedPoint));
        }
    }

    @Test
    void 주문_취소_실패_WAITING_아닌경우() {
        // given
        Order order = Order.of(
                shop,
                user,
                "홍길동",
                "010-1234-5678",
                "서울 강남구 oo",
                "oo아파트 101동 1001호",
                "양파 빼주세요",
                null,
                5000,
                23000,
                18000,
                LocalDateTime.now(),
                OrderStatus.COOKING
        );
        ReflectionTestUtils.setField(order, "id", 101L);
        when(orderEntityFinder.getOrderById(101L)).thenReturn(order);

        // when & then
        assertThatThrownBy(() -> orderUserService.cancelOrder(101L))
                .isInstanceOf(OrderNotCancelableException.class)
                .extracting(e -> ((OrderNotCancelableException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.ORDER_NOT_CANCELABLE);
        verifyNoInteractions(userService);
    }
}
