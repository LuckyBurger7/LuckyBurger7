package org.example.luckyburger.domain.shop.dto.response;

import lombok.Builder;

@Builder
public record ShopDashboardResponse(
        Long shopId,
        Long todayOrderCount,
        Long todayTotalSales,
        Double averageRating
) {
    public static ShopDashboardResponse of(
            Long shopId,
            Long todayOrderCount,
            Long todayTotalSales,
            Double averageRating) {
        return ShopDashboardResponse.builder()
                .shopId(shopId)
                .todayOrderCount(todayOrderCount)
                .todayTotalSales(todayTotalSales)
                .averageRating(averageRating)
                .build();
    }
}
