package org.example.luckyburger.domain.order.service;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.auth.service.OwnerEntityFinder;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.order.code.OrderErrorCode;
import org.example.luckyburger.domain.order.dto.request.OrderUpdateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.OrderStatusInvalidUpdateException;
import org.example.luckyburger.domain.order.exception.UnauthorizedOrderAccessException;
import org.example.luckyburger.domain.order.repository.OrderMenuRepository;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.user.entity.User;
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
public class OrderOwnerServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderMenuRepository orderMenuRepository;
    @Mock
    private OwnerEntityFinder ownerEntityFinder;
    @Mock
    private AccountEntityFinder accountEntityFinder;
    @Mock
    private OrderEntityFinder orderEntityFinder;
    @Mock
    private OrderMenuEntityFinder orderMenuEntityFinder;
    @Mock
    private UserService userService;
    @InjectMocks
    private OrderOwnerService orderOwnerService;

    private Account account;
    private Owner owner;
    private User user;
    private Shop shop;
    private Cart cart;

    @BeforeEach
    void setUp() {
        AuthAccount principal = new AuthAccount(1L, "test@example.com", AccountRole.ROLE_OWNER);
        Authentication auth = new TestingAuthenticationToken(
                principal, null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        account = Account.of("user@test.com", "홍길동", "password1!", AccountRole.ROLE_OWNER);
        shop = Shop.of("럭키버거 강남점", BusinessStatus.OPEN, "서울 강남구", "상세 주소");
        user = User.of(account, "010-0000-0000", "서울 강남구 oo", "oo아파트 101동 1001호");
        cart = Cart.of(user, 0);

        ReflectionTestUtils.setField(user, "id", 101L);
        ReflectionTestUtils.setField(shop, "id", 201L);
        ReflectionTestUtils.setField(cart, "id", 301L);
        owner = Owner.of(account, shop);

        when(ownerEntityFinder.getOwnerByAccountId(anyLong())).thenReturn(owner);
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
        OrderResponse response = orderOwnerService.getOrderResponse(101L);

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
        Shop otherShop = Shop.of("럭키버거 서초점", BusinessStatus.OPEN, "서울 강남구", "상세 주소");
        ReflectionTestUtils.setField(otherShop, "id", 99L);

        Order order = Order.of(
                otherShop,
                user,
                "다른지점",
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
        assertThatThrownBy(() -> orderOwnerService.getOrderResponse(777L))
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

        when(orderRepository.findByShop(any(Shop.class), any(Pageable.class))).thenReturn(orderPage);

        Menu m = Menu.of("치즈버거", MenuCategory.HAMBURGER, 7000);
        ShopMenu sm = ShopMenu.of(shop, m, ShopMenuStatus.ON_SALE, 0);
        OrderMenu om1 = OrderMenu.of(o1, sm, 2);
        OrderMenu om2 = OrderMenu.of(o2, sm, 1);

        when(orderMenuRepository.findAllByOrderInWithMenu(orders))
                .thenReturn(List.of(om1, om2));

        // when
        Page<OrderResponse> page = orderOwnerService.getAllOrderResponse(pageable);

        // then
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.getContent().get(0).items()).isNotEmpty();
    }

    @Test
    void 주문_전체조회_성공_비어있음() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Order> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(orderRepository.findByShop(any(Shop.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<OrderResponse> result = orderOwnerService.getAllOrderResponse(pageable);

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

        var request = new OrderUpdateRequest(OrderStatus.CANCEL);

        // when
        orderOwnerService.updateOrderStatus(101L, request);

        // then
        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCEL);
        // 사용 포인트 환원 호출 확인
        Integer usedPoint = order.getPoint();
        if (usedPoint != null && usedPoint > 0) {
            verify(userService).addPoints(eq(user), eq(usedPoint));
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
        var request = new OrderUpdateRequest(OrderStatus.CANCEL);

        // when & then
        assertThatThrownBy(() -> orderOwnerService.updateOrderStatus(101L, request))
                .isInstanceOf(OrderStatusInvalidUpdateException.class)
                .extracting(e -> ((OrderStatusInvalidUpdateException) e).getErrorCode())
                .isEqualTo(OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION);
        verifyNoInteractions(userService);
    }
}
