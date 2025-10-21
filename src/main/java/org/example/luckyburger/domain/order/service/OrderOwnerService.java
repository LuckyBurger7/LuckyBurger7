package org.example.luckyburger.domain.order.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.auth.service.OwnerEntityFinder;
import org.example.luckyburger.domain.order.dto.request.OrderUpdateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderMenuResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.UnauthorizedOrderAccessException;
import org.example.luckyburger.domain.order.repository.OrderMenuRepository;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.user.constant.UserConstant;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderOwnerService {
    private final OrderRepository orderRepository;
    private final OrderMenuRepository orderMenuRepository;
    private final OrderEntityFinder orderEntityFinder;
    private final OrderMenuEntityFinder orderMenuEntityFinder;
    private final OwnerEntityFinder ownerEntityFinder;
    private final AccountEntityFinder accountEntityFinder;
    private final UserService userService;

    @Transactional(readOnly = true)
    public OrderResponse getOrderResponse(Long orderId) {
        Owner owner = getOwnerByAuthAccount();
        Order order = orderEntityFinder.getOrderById(orderId);

        if (!order.getShop().getId().equals(owner.getShop().getId())) {
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
        Owner owner = getOwnerByAuthAccount();

        // 주문 페이징 조회
        Page<Order> orderPage = orderRepository.findByShop(owner.getShop(), pageable);
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
    public void updateOrderStatus(Long orderId, OrderUpdateRequest request) {
        Owner owner = getOwnerByAuthAccount();
        Order order = orderEntityFinder.getOrderById(orderId);
        User user = order.getUser();
        OrderStatus status = request.status();

        if (!order.getShop().getId().equals(owner.getShop().getId())) {
            throw new UnauthorizedOrderAccessException();
        }

        order.updateStatusByOwner(status);

        if (status == OrderStatus.CANCEL) {

            // TODO: 결제 취소 및 환불

            // TODO: 쿠폰 적용 취소

            // 적립금 적용 취소
            Integer usedPoint = order.getPoint();
            if (usedPoint != null && usedPoint > 0) {
                userService.addPoints(user.getId(), usedPoint);
            }

            // TODO: ShopMenu 판매량 증가 취소

        } else if (status == OrderStatus.COMPLETED) {
            int addedPoint = (int) (order.getTotalPrice() * UserConstant.POINT_RATIO);
            userService.addPoints(user.getId(), addedPoint);
        }
    }

    @Transactional(readOnly = true)
    public Owner getOwnerByAuthAccount() {
        Account ownerAccount = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        return ownerEntityFinder.getOwnerByAccount(ownerAccount);
    }
}


