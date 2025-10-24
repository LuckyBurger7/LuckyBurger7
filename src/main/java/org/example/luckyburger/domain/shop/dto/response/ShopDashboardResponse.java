package org.example.luckyburger.domain.shop.dto.response;

import lombok.Builder;

@Builder
public record ShopDashboardResponse(
        Long todayOrderCount,
        Long todayTotalSales,
        Double averageRating
) {
    public static ShopDashboardResponse of(
            Long todayOrderCount,
            Long todayTotalSales,
            Double averageRating) {
        return ShopDashboardResponse.builder()
                .todayOrderCount(todayOrderCount)
                .todayTotalSales(todayTotalSales)
                .averageRating(averageRating)
                .build();
    }
}
