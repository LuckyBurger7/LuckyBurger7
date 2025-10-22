package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.exception.OrderNotFoundException;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

    public Long getTotalSales(Shop shop) {
        return orderRepository.findSumOfTotalPriceByShop(shop);
    }

    public Long getTotalSalesByShopAndToday(Shop shop, LocalDateTime midnightToday) {
        return orderRepository.findSumOfTotalPriceByShopAndOrderDateAfter(shop, midnightToday);
    }

    public Integer getCountOrderByShopAndToday(Shop shop, LocalDateTime midnightToday) {
        return orderRepository.countByShopAndOrderDateAfter(shop, midnightToday);
    }
}
