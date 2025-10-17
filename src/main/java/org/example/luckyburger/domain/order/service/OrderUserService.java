package org.example.luckyburger.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.cart.service.CartService;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderCreateResponse;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.*;
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
    public OrderCreateResponse createOrder(AuthAccount account, OrderCreateRequest request) {
        Account userAccount = accountEntityFinder.getAccountById(account.accountId());
        User user = userEntityFinder.getUserByAccount(userAccount);
        Shop shop = shopEntityFinder.getShopById(request.shopId());
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        // TODO: cartMenus 기져오기 (CartEntityFinder -> getMenus 메서드 추가 또는 CartMenuEntityFinder 이용)
        List<CartMenu> cartMenus = cartMenuEntityFinder.getByCartId(cart.getId());
        if (cartMenus.isEmpty()) {
            throw new EmptyCartOrderException();
        }

        // 매장 영업 중인지 확인
        if (shop.getStatus() != BusinessStatus.OPEN) {
            throw new ShopNotOpenedOrderException();
        }

        // 총 금액 계산
        long subtotal = cartMenus.stream()
                .mapToLong(cartMenu -> cartMenu.getShopMenu().getMenu().getPrice() * cartMenu.getQuantity())
                .sum();

        // 할인 금액 계산
        long discount = 0L;

        // TODO: 쿠폰 불러오기 및 금액 확인

        //적립금 확인
        int usePoint = request.point() == null ? 0 : request.point();
        int userPoint = user.getPoint();
        if (usePoint > userPoint) {
            throw new PointExceedBalanceOrderException();
        }
        if (usePoint > subtotal) {
            usePoint = (int) subtotal;
        }
        discount += usePoint;

        long pay = subtotal - discount;
        if (pay < 0) throw new NegativePayOrderException();

        // 주문 생성
        Order order = Order.of(
                shop,
                user,
                request.receiver(),
                request.phone(),
                request.address(),
                request.street(),
                request.request(),
                null,
                usePoint,
                subtotal,
                pay,
                LocalDateTime.now(),
                OrderStatus.WAITING
        );
        orderRepository.save(order);

        for (CartMenu cartMenu : cartMenus) {
            OrderMenu orderMenu = OrderMenu.of(order, cartMenu.getShopMenu(), cartMenu.getQuantity());
            orderMenuRepository.save(orderMenu);
        }

        // TODO: 장바구니 비우기 및 삭제
        cartService.clear(cart.getId());

        // TODO: 쿠폰 사용 처리

        // TODO: 적립금 차감
        if (usePoint > 0) {
            userService.deductPoints(user.getId(), usePoint);
        }

        // TODO: 적립금 추가 (할인 적용 전 가격 기준 5% 적립?)
        int addedPoint = (int) (subtotal * 0.05); //TODO: 상수 적용
        userService.addPoints(user.getId(), addedPoint);

        // TODO: ShopMenu 별 판매량 증가

        // TODO: 결제 연동

        List<OrderMenuResponse> orderMenuResponses = cartMenus.stream()
                .map(cartMenu -> OrderMenuResponse.of(
                        cartMenu.getShopMenu().getId(),
                        cartMenu.getShopMenu().getMenu().getName(),
                        cartMenu.getShopMenu().getMenu().getPrice(),
                        cartMenu.getQuantity()
                ))
                .toList();

        return OrderCreateResponse.of(
                order.getId(),
                shop.getId(),
                order.getReceiver(),
                order.getPhone(),
                order.getAddress(),
                order.getStreet(),
                order.getRequest(),
                null,
                usePoint,
                addedPoint,
                OrderCreateResponse.Amount.of(subtotal, discount, pay),
                orderMenuResponses,
                order.getOrderDate(),
                order.getStatus()
        );

    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(AuthAccount account, Long orderId) {
        Account userAccount = accountEntityFinder.getAccountById(account.accountId());
        User user = userEntityFinder.getUserByAccount(userAccount);
        Order order = orderEntityFinder.getOrderById(orderId);

        if (!order.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedOrderAccessException();
        }

        List<OrderMenuResponse> items = orderMenuEntityFinder.getAllOrderMenu(orderId).stream()
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
    public Page<OrderResponse> getAllOrder(AuthAccount account, Pageable pageable) {
        Account userAccount = accountEntityFinder.getAccountById(account.accountId());
        User user = userEntityFinder.getUserByAccount(userAccount);

        // 주문 ID 페이징 우선 조회
        Page<Long> idPage = orderRepository.findIdsByUserId(user.getId(), pageable);
        if (idPage.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, idPage.getTotalElements());
        }

        List<Long> orderIds = idPage.getContent();

        // 주문 ID 목록으로 주문 조회
        List<Order> orders = orderRepository.findWithShopByIdIn(orderIds);

        // 주문 ID 목록으로 주문 메뉴 전체 조회
        List<OrderMenu> allOrderMenus = orderMenuRepository.findAllByOrderIdsInWithMenu(orderIds);

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
        return new PageImpl<>(contents, pageable, idPage.getTotalElements());
    }

    
}


