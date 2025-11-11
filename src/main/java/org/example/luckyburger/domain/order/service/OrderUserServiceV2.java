package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.cart.entity.Cart;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.cart.service.CartEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuEntityFinder;
import org.example.luckyburger.domain.cart.service.CartMenuService;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.service.UserCouponEntityFinder;
import org.example.luckyburger.domain.order.dto.cache.OrderFormCache;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderCouponResponse;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
import org.example.luckyburger.domain.order.dto.response.OrderPrepareResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.*;
import org.example.luckyburger.domain.order.repository.OrderMenuRepository;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderUserServiceV2 {

    private static final String ORDER_FORM_KEY_PREFIX = "orderForm:";
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final UserEntityFinder userEntityFinder;
    private final UserService userService;
    private final CartMenuService cartMenuService;
    private final ShopEntityFinder shopEntityFinder;
    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final CartEntityFinder cartEntityFinder;
    private final CartMenuEntityFinder cartMenuEntityFinder;
    private final UserCouponEntityFinder userCouponEntityFinder;
    private final OrderFormCacheService orderFormCacheService;

    @Transactional(readOnly = true)
    public OrderPrepareResponse prepareOrderResponse() {
        User user = getUser();
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());

        // 장바구니 메뉴 조회
        List<CartMenu> cartMenus = cartMenuEntityFinder.getAllCartMenuByCartId(cart.getId());
        if (cartMenus.isEmpty()) {
            throw new EmptyOrderException();
        }

        Shop shop = shopEntityFinder.getShopById(cartMenus.get(0).getShopMenu().getShop().getId());

        // 매장 영업 중인지 확인
        if (shop.getStatus() != BusinessStatus.OPEN) {
            throw new ShopNotOpenedException();
        }

        // Redis에 주문서 캐시 저장 (shopMenuId, price, quantity)
        String redisKey = ORDER_FORM_KEY_PREFIX + user.getId();

        List<OrderFormCache> orderForms = cartMenus.stream()
                .map(OrderFormCache::from)
                .toList();

        // Redis 저장
        orderFormCacheService.saveCache(redisKey, orderForms);

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
                user.getAccount().getName(),
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
        Shop shop = shopEntityFinder.getShopById(request.shopId());

        // 매장 영업 중인지 확인
        if (shop.getStatus() != BusinessStatus.OPEN) {
            throw new ShopNotOpenedException();
        }

        User user = getUser();

        String redisKey = ORDER_FORM_KEY_PREFIX + user.getId();

        // 캐싱된 주문 정보 조회
        List<OrderFormCache> orderForms = orderFormCacheService.getCache(redisKey);
        if (orderForms == null || orderForms.isEmpty()) {
            throw new EmptyOrderException();
        }

        Map<Long, OrderFormCache> orderFormMap = orderForms.stream()
                .collect(Collectors.toMap(OrderFormCache::getShopMenuId, Function.identity()));

        List<Long> shopMenuIds = new ArrayList<>();
        for (OrderFormCache forms : orderForms) {
            shopMenuIds.add(forms.getShopMenuId());
        }

        // 캐싱된 정보를 바탕으로 ShopMenu 리스트 조회
        List<ShopMenu> shopMenus = shopMenuEntityFinder.getAllShopMenuById(shopMenuIds);

        for (ShopMenu shopMenu : shopMenus) {
            // 해당 메뉴가 판매 중지 되었을 때
            if (shopMenu.getStatus() != ShopMenuStatus.ON_SALE) {
                throw new ShopMenuNotOnSaleException();
            }

            // 캐시 된 가격의 정보와 shopMenu의 가격 정보가 일치 하지 않을 때
            OrderFormCache form = orderFormMap.get(shopMenu.getId());
            if (form.getPrice() != shopMenu.getMenu().getPrice()) {
                throw new ShopMenuPriceChangedException();
            }
        }

        // 총 금액 계산
        long subtotal = orderForms.stream()
                .mapToLong(dto -> dto.getPrice() * dto.getQuantity())
                .sum();

        // 할인 금액 계산
        long discount = 0L;

        // 해당 쿠폰 조회 및 사용
        UserCoupon userCoupon = null;
        if (request.couponId() != null) {
            userCoupon = userCouponEntityFinder.getVerifiedUserCouponByCouponId(request.couponId());
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
        List<OrderMenu> savedOrderMenus = shopMenus.stream()
                .map(shopMenu -> {
                    OrderFormCache form = orderFormMap.get(shopMenu.getId());
                    return OrderMenu.of(savedOrder, shopMenu, form.getQuantity());
                })
                .toList();

        orderMenuRepository.saveAll(savedOrderMenus);

        // 주문서 삭제
        orderFormCacheService.deleteCache(redisKey);

        // 장바구니 비우기 및 삭제
        Cart cart = cartEntityFinder.getCartByUserId(user.getId());
        cartMenuService.clear(cart);

        List<OrderMenuResponse> orderMenuResponses = savedOrderMenus.stream()
                .map(OrderMenuResponse::from)
                .toList();

        return OrderResponse.of(
                savedOrder.getId(),
                savedOrder.getShop().getId(),
                savedOrder.getReceiver(),
                savedOrder.getPhone(),
                savedOrder.getAddress(),
                savedOrder.getStreet(),
                savedOrder.getRequest(),
                savedOrder.getCoupon() != null ? savedOrder.getCoupon().getId() : null,
                savedOrder.getPoint(),
                OrderResponse.Amount.of(subtotal, pay),
                orderMenuResponses,
                savedOrder.getOrderDate(),
                savedOrder.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public User getUser() {
        return userEntityFinder.getUserByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());
    }
}
