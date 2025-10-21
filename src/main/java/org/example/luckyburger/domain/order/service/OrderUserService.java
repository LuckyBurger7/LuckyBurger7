package org.example.luckyburger.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.cart.service.CartService;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderCouponResponse;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
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
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderUserService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderFormRepository orderFormRepository;
    private final OrderEntityFinder orderEntityFinder;
    private final OrderMenuEntityFinder orderMenuEntityFinder;
    private final UserEntityFinder userEntityFinder;
    private final AccountEntityFinder accountEntityFinder;
    private final UserService userService;
    private final ShopEntityFinder shopEntityFinder;
    private final CartService cartService;
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;

    @Transactional
    public OrderPrepareResponse prepareOrderResponse() {
        Account userAccount = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        User user = userEntityFinder.getUserByAccount(userAccount);
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        // TODO: cartMenus 가져오기 (CartEntityFinder -> getMenus 메서드 추가 또는 CartMenuEntityFinder 이용)
        List<CartMenu> cartMenus = cartMenuEntityFinder.getByCartId(cart.getId());
        if (cartMenus.isEmpty()) {
            throw new EmptyOrderException();
        }

        // 주문서 저장 (이전 주문서 삭제)
        orderFormRepository.deleteAllByUser(user);
        for (CartMenu cartMenu : cartMenus) {
            OrderForm orderForm = OrderForm.of(user, cartMenu.getShopMenu(), cartMenu.getQuantity());
            orderFormRepository.save(orderForm);
        }

        Shop shop = shopEntityFinder.getShopById(cartMenus.get(0).getShopMenu().getShop().getId());

        // 매장 영업 중인지 확인
        if (shop.getStatus() != BusinessStatus.OPEN) {
            throw new ShopNotOpenedException();
        }

        // 총 금액
        long totalPrice = cart.getTotalPrice();

        // TODO: 쿠폰 불러오기
        List<OrderCouponResponse> coupons = null;

        // 보유 적립금
        int userPoint = user.getPoint();

        List<OrderMenuResponse> orderMenuResponses = cartMenus.stream()
                .map(cartMenu -> OrderMenuResponse.of(
                        cartMenu.getShopMenu().getId(),
                        cartMenu.getShopMenu().getMenu().getName(),
                        cartMenu.getShopMenu().getMenu().getPrice(),
                        cartMenu.getQuantity()
                ))
                .toList();

        return OrderPrepareResponse.of(
                shop.getName(),
                userAccount.getName(),
                user.getPhone(),
                user.getAddress(),
                user.getStreet(),
                coupons,
                userPoint,
                totalPrice,
                orderMenuResponses
        );
    }

    @Transactional
    public OrderResponse createOrderResponse(OrderCreateRequest request) {
        User user = getUserByAuthAccount();

        // 주문서 조회
        List<OrderForm> orderForms = orderFormRepository.findAllByUser(user);
        if (orderForms.isEmpty()) {
            throw new EmptyOrderException();
        }

        Shop shop = shopEntityFinder.getShopById(request.shopId());

        // 매장 영업 중인지 확인
        if (shop.getStatus() != BusinessStatus.OPEN) {
            throw new ShopNotOpenedException();
        }

        // 총 금액
        long subtotal = orderForms.stream()
                .mapToLong(orderForm -> orderForm.getShopMenu().getMenu().getPrice() * orderForm.getQuantity())
                .sum();

        // 할인 금액 계산
        long discount = 0L;

        // TODO: 해당 쿠폰 조회 및 금액 확인
        Coupon coupon = null;
        Long couponId = null;

        // 적립금 확인
        int usePoint = request.point() == null ? 0 : request.point();
        int userPoint = user.getPoint();
        if (usePoint > userPoint) {
            throw new PointExceedBalanceException();
        }
        if (usePoint > subtotal) {
            usePoint = (int) subtotal;
        }
        discount += usePoint;

        // 실제 결제 금액 계산
        long pay = subtotal - discount;
        if (pay < 0) throw new NegativePayOrderException();

        // TODO: 쿠폰 사용 처리

        // TODO: 적립금 차감
        if (usePoint > 0) {
            userService.deductPoints(user.getId(), usePoint);
        }

        // TODO: 결제 연동

        // 주문 생성
        Order order = Order.of(
                shop,
                user,
                request.receiver(),
                request.phone(),
                request.address(),
                request.street(),
                request.request(),
                coupon,
                usePoint,
                subtotal,
                pay,
                LocalDateTime.now(),
                OrderStatus.WAITING
        );
        Order savedOrder = orderRepository.save(order);

        // OrderForm -> OrderMenu 복사
        for (OrderForm orderForm : orderForms) {
            OrderMenu orderMenu = OrderMenu.of(savedOrder, orderForm.getShopMenu(), orderForm.getQuantity());
            orderMenuRepository.save(orderMenu);
        }
        // 주문서 삭제
        orderFormRepository.deleteAllByUser(user);

        // TODO: 장바구니 비우기 및 삭제
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());
        cartService.clear(cart.getId());

        // TODO: ShopMenu 별 판매량 증가

        List<OrderMenuResponse> orderMenuResponses = orderForms.stream()
                .map(orderForm -> OrderMenuResponse.of(
                        orderForm.getShopMenu().getId(),
                        orderForm.getShopMenu().getMenu().getName(),
                        orderForm.getShopMenu().getMenu().getPrice(),
                        orderForm.getQuantity()
                ))
                .toList();

        return OrderResponse.of(
                order.getId(),
                shop.getId(),
                order.getReceiver(),
                order.getPhone(),
                order.getAddress(),
                order.getStreet(),
                order.getRequest(),
                couponId,
                usePoint,
                OrderResponse.Amount.of(subtotal, pay),
                orderMenuResponses,
                order.getOrderDate(),
                order.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponse(Long orderId) {
        User user = getUserByAuthAccount();
        Order order = orderEntityFinder.getOrderById(orderId);

        if (!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedOrderAccessException();
        }

        List<OrderMenuResponse> items = orderMenuEntityFinder.getAllOrderMenuByOrderId(orderId).stream()
                .map(item -> OrderMenuResponse.of(
                        item.getShopMenu().getId(),
                        item.getShopMenu().getMenu().getName(),
                        item.getShopMenu().getMenu().getPrice(),
                        item.getQuantity()
                ))
                .toList();
        return OrderResponse.from(order, items);
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getAllOrderResponse(Pageable pageable) {
        User user = getUserByAuthAccount();

        // 주문 페이징 조회
        Page<Order> orderPage = orderRepository.findByUserId(user.getId(), pageable);
        if (orderPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, orderPage.getTotalElements());
        }

        List<Order> orders = orderPage.getContent();

        // 주문 메뉴 목록 조회
        List<OrderMenu> allOrderMenus = orderMenuRepository.findAllByOrderInWithMenu(orders);

        // 주문 메뉴 그룹핑
        Map<Long, List<OrderMenu>> itemsByOrderId = allOrderMenus.stream()
                .collect(Collectors.groupingBy(om -> om.getOrder().getId()));

        List<OrderResponse> contents = orders.stream().map(order -> {
            List<OrderMenuResponse> items = itemsByOrderId.getOrDefault(order.getId(), List.of())
                    .stream()
                    .map(om -> OrderMenuResponse.of(
                            om.getShopMenu().getId(),
                            om.getShopMenu().getMenu().getName(),
                            om.getShopMenu().getMenu().getPrice(),
                            om.getQuantity()
                    ))
                    .toList();

            return OrderResponse.from(order, items);
        }).toList();

        return new PageImpl<>(contents, pageable, orderPage.getTotalElements());
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        User user = getUserByAuthAccount();
        Order order = orderEntityFinder.getOrderById(orderId);

        if (!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedOrderAccessException();
        }

        order.cancelByUser();

        // TODO: 결제 취소 및 환불

        // TODO: 쿠폰 적용 취소

        // 적립금 적용 취소
        Integer usedPoint = order.getPoint();
        if (usedPoint != null && usedPoint > 0) {
            userService.addPoints(user.getId(), usedPoint);
        }

        // TODO: ShopMenu 판매량 증가 취소
    }

    @Transactional(readOnly = true)
    public User getUserByAuthAccount() {
        Account userAccount = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        return userEntityFinder.getUserByAccount(userAccount);
    }
}


