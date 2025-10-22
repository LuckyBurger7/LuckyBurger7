package org.example.luckyburger.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuService;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.service.CouponEntityFinder;
import org.example.luckyburger.domain.coupon.service.UserCouponEntityFinder;
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
    private final CartMenuService cartMenuService;
    private final ShopEntityFinder shopEntityFinder;
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;
    private final UserCouponEntityFinder userCouponEntityFinder;
    private final CouponEntityFinder couponEntityFinder;

    @Transactional
    public OrderPrepareResponse prepareOrderResponse() {
        Account userAccount = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        User user = userEntityFinder.getUserByAccount(userAccount);
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        // 장바구니 메뉴 조회
        List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId());
        if (cartMenus.isEmpty()) {
            throw new EmptyOrderException();
        }

        // 주문서 저장 (이전 주문서 삭제)
        orderFormRepository.deleteAllByUser(user);
        List<OrderForm> orderFormsToSave = cartMenus.stream()
                .map(cartMenu -> OrderForm.of(user, cartMenu.getShopMenu(), cartMenu.getQuantity()))
                .toList();
        orderFormRepository.saveAll(orderFormsToSave);

        Shop shop = shopEntityFinder.getShopById(cartMenus.get(0).getShopMenu().getShop().getId());

        // 매장 영업 중인지 확인
        if (shop.getStatus() != BusinessStatus.OPEN) {
            throw new ShopNotOpenedException();
        }

        // 총 금액
        long totalPrice = cart.getTotalPrice();

        // 유저 보유 쿠폰 조회
        List<OrderCouponResponse> coupons = userCouponEntityFinder.getAllVerifiedUserCouponByUserId(user.getId()).stream()
                .map(OrderCouponResponse::from)
                .toList();

        // 보유 적립금
        int userPoint = user.getPoint();

        List<OrderMenuResponse> orderMenuResponses = cartMenus.stream()
                .map(OrderMenuResponse::from)
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

        // 총 금액 계산
        long subtotal = orderForms.stream()
                .mapToLong(orderForm -> orderForm.getShopMenu().getMenu().getPrice() * orderForm.getQuantity())
                .sum();

        // 할인 금액 계산
        long discount = 0L;

        // 해당 쿠폰 조회 및 사용
        UserCoupon userCoupon = null;
        if (request.couponId() != null) {
            Coupon coupon = couponEntityFinder.getCouponById(request.couponId());
            userCoupon = userCouponEntityFinder.getVerifiedUserCouponByCoupon(coupon);
            discount += userCoupon.getCoupon().calculateDiscount(subtotal);
            // 쿠폰 사용
            userCoupon.useCoupon();
        }

        // 적립금 확인
        int usePoint = request.point() == null ? 0 : request.point();
        int userPoint = user.getPoint();

        if (usePoint > userPoint) {
            throw new PointExceedBalanceException();
        }
        if (usePoint > (subtotal - discount)) {
            usePoint = (int) (subtotal - discount);
        }

        discount += usePoint;

        // 적립금 차감
        if (usePoint > 0) {
            userService.deductPoints(user, usePoint);
        }

        // 실제 결제 금액 계산
        long pay = subtotal - discount;
        if (pay < 0) throw new NegativePayOrderException();

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
                userCoupon != null ? userCoupon.getCoupon() : null,
                usePoint,
                subtotal,
                pay,
                LocalDateTime.now(),
                OrderStatus.WAITING
        );
        Order savedOrder = orderRepository.save(order);

        // OrderForm -> OrderMenu 복사
        List<OrderMenu> orderMenusToSave = orderForms.stream()
                .map(orderForm -> OrderMenu.of(savedOrder, orderForm.getShopMenu(), orderForm.getQuantity()))
                .toList();
        orderMenuRepository.saveAll(orderMenusToSave);

        // 주문서 삭제
        orderFormRepository.deleteAllByUser(user);

        // 장바구니 비우기 및 삭제
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());
        cartMenuService.clear(cart.getId());

        List<OrderMenuResponse> orderMenuResponses = orderForms.stream()
                .map(OrderMenuResponse::from)
                .toList();

        return OrderResponse.of(
                savedOrder.getId(),
                shop.getId(),
                savedOrder.getReceiver(),
                savedOrder.getPhone(),
                savedOrder.getAddress(),
                savedOrder.getStreet(),
                savedOrder.getRequest(),
                savedOrder.getCoupon() != null ? savedOrder.getCoupon().getId() : null,
                usePoint,
                OrderResponse.Amount.of(subtotal, pay),
                orderMenuResponses,
                savedOrder.getOrderDate(),
                savedOrder.getStatus()
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
                .map(OrderMenuResponse::from)
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
                    .map(OrderMenuResponse::from)
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

        // 쿠폰 적용 취소
        if (order.getCoupon() != null) {
            Coupon coupon = couponEntityFinder.getCouponById(order.getCoupon().getId());
            UserCoupon userCoupon = userCouponEntityFinder.getUserCouponByCoupon(coupon);
            userCoupon.restoreCoupon();
        }

        // 적립금 적용 취소
        Integer usedPoint = order.getPoint();
        if (usedPoint != null && usedPoint > 0) {
            userService.addPoints(user, usedPoint);
        }
    }

    @Transactional(readOnly = true)
    public User getUserByAuthAccount() {
        Account userAccount = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        return userEntityFinder.getUserByAccount(userAccount);
    }
}


