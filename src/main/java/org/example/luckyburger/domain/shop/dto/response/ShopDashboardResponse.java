package org.example.luckyburger.domain.shop.dto.response;

import lombok.Builder;

@Builder
public record ShopDashboardResponse(
        Integer todayOrderCount,
        Long todayTotalSales,
        Double averageRating
) {
    public static ShopDashboardResponse of(
            Integer todayOrderCount,
            Long todayTotalSales,
            Double averageRating) {
        return ShopDashboardResponse.builder()
                .todayOrderCount(todayOrderCount)
                .todayTotalSales(todayTotalSales)
                .averageRating(averageRating)
                .build();
    }
}
