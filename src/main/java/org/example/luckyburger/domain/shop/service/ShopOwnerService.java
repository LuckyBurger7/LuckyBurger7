package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.service.ReviewEntityFinder;
import org.example.luckyburger.domain.shop.dto.response.ShopDashboardResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopOwnerService {

    private final ShopEntityFinder shopEntityFinder;
    private final OrderEntityFinder orderEntityFinder;
    private final ReviewEntityFinder reviewEntityFinder;

    // 상점의 상태를 변경
    @Transactional
    public Shop updateStatus(Long shopId,
                             BusinessStatus shopStatus) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        shopEntity.updateShopStatus(shopStatus);

        return shopEntity;
    }

    @Transactional(readOnly = true)
    public int getTotalSaleByMonthWithShopId(Long shopId, LocalDate localDate) {

        int year = localDate.getYear();
        int month = localDate.getMonthValue();

        LocalDateTime start = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime end = LocalDate.of(year, month,
                        YearMonth.of(year, month).lengthOfMonth())
                .atTime(LocalTime.MAX);

        int totalSaleByMonth = 0;

        List<Order> orderListByShopId = orderEntityFinder.getAllOrderByShopId(shopId);

        for (Order order : orderListByShopId) {
            if (order.getOrderDate().isBefore(end) && order.getOrderDate().isAfter(start)) {
                totalSaleByMonth += order.getTotalPrice();
            }
        }

        return totalSaleByMonth;
    }

    @Transactional(readOnly = true)
    public Page<OrderResponse> getOrderTodayByShop(LocalDateTime start, LocalDateTime end, Long shopId, int page, int size) {

        List<Order> orderList = new ArrayList<>();

        List<Order> orderListByShopId = orderEntityFinder.getAllOrderByShopId(shopId);

        for (Order order : orderListByShopId) {
            if (order.getOrderDate().isBefore(end) && order.getOrderDate().isAfter(start)) {
                orderList.add(order);
            }
        }

        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orderPage = new PageImpl<>(orderList, pageable, size);

        return orderPage.map(order -> OrderResponse.of(
                order.getId(),
                order.getShop().getId(),
                order.getReceiver(),
                order.getPhone(),
                order.getAddress(),
                order.getStreet(),
                order.getRequest(),
                order.getCoupon() != null ? order.getCoupon().getId() : null,
                order.getPoint(),
                OrderResponse.Amount.of(order.getTotalPrice(), order.getPay()),
                Collections.emptyList(),
                order.getOrderDate(),
                order.getStatus()
        ));
    }

    @Transactional(readOnly = true)
    public ShopDashboardResponse getShopDashboardByShopId(Long shopId) {

        Shop shop = shopEntityFinder.getShopById(shopId);

        Integer todayOrderCount = getTodayOrderCount(shop);
        Long todayTotalSales = getTotalSaleToday(shop);
        Double averageRating = getRatingByShop(shop);

        return ShopDashboardResponse.of(
                todayOrderCount,
                todayTotalSales,
                averageRating
        );
    }

    private Integer getTodayOrderCount(Shop shop) {
        LocalDateTime midnightToday = LocalDate.now().atStartOfDay();

        return orderEntityFinder.getCountOrderByShopAndToday(shop, midnightToday);
    }

    private Long getTotalSaleToday(Shop shop) {

        LocalDateTime midnightToday = LocalDate.now().atStartOfDay();

        return orderEntityFinder.getTotalSalesByShopAndToday(shop, midnightToday);

    }

    private Double getRatingByShop(Shop shop) {

        return reviewEntityFinder.getAvgOfRatingByShop(shop);
    }
}
