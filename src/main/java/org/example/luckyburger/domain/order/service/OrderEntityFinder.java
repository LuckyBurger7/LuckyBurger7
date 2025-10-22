package org.example.luckyburger.domain.order.service;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.OrderNotFoundException;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class OrderEntityFinder {
    private final OrderRepository orderRepository;

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(OrderNotFoundException::new);
    }

    public List<Order> getAllOrderByShopId(Long shopId) {
        List<Order> orders = orderRepository.findAllByShopId(shopId);
        return orders;
    }

    public Long getTotalPrice(Shop shop) {
        return orderRepository.findSumOfTotalPriceByShop(shop);
    }

    public long sumMonthlySalesTotal(Long shopId, LocalDateTime start, LocalDateTime end) {
        Long total = orderRepository.sumMonthlyPaidByShopId(shopId, OrderStatus.COMPLETED, start, end);
        return total == null ? 0L : total;
    }
}
