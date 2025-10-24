package org.example.luckyburger.domain.order.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.order.exception.OrderNotFoundException;
import org.example.luckyburger.domain.order.repository.OrderRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.statistic.dto.response.MonthTotalSalesResponse;
import org.example.luckyburger.domain.statistic.dto.response.ShopTotalSalesResponse;
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

    public Long getTotalSales(Shop shop) {
        return orderRepository.findSumOfTotalPriceByShop(shop);
    }

    public Long getTotalSalesByShopAndToday(Shop shop, LocalDateTime midnightToday) {
        return orderRepository.findSumOfTotalPriceByShopAndOrderDateAfter(shop, midnightToday);
    }

    public Long getCountOrderByShopAndCompletedAndToday(Shop shop, LocalDateTime midnightToday) {
        return orderRepository.countByShopAndStatusAndOrderDateAfter(shop, OrderStatus.COMPLETED, midnightToday);
    }

    public long sumMonthlySalesTotal(Long shopId, LocalDateTime start, LocalDateTime end) {
        Long total = orderRepository.sumMonthlyPaidByShopId(shopId, OrderStatus.COMPLETED, start, end);
        return total == null ? 0L : total;
    }

    public List<MonthTotalSalesResponse> getAllMonthTotalSalesResponse() {
        return orderRepository.findAllMonthTotalSalesResponse();
    }

    public List<ShopTotalSalesResponse> getAllShopTotalSalesResponseOrderByAsc() {
        return orderRepository.findAllShopTotalSalesResponseOrderByAsc();
    }

    public List<ShopTotalSalesResponse> getAllShopTotalSalesResponseOrderByDesc() {
        return orderRepository.findAllShopTotalSalesResponseOrderByDesc();
    }

    public Long getCountOrderByCompleted() {
        return orderRepository.countByStatus(OrderStatus.COMPLETED);
    }
}
