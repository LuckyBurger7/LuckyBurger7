package org.example.luckyburger.domain.statistic.dto.response;

import lombok.Builder;

@Builder
public record AdminDashboardResponse(
        Long totalShopCount,
        Long totalOrderCount,
        Long activeCoupon,
        Long totalSales
) {
    public static AdminDashboardResponse of(
            Long totalShopCount,
            Long totalOrderCount,
            Long activeCoupon,
            Long totalSales) {
        return AdminDashboardResponse.builder()
                .totalShopCount(totalShopCount)
                .totalOrderCount(totalOrderCount)
                .activeCoupon(activeCoupon)
                .totalSales(totalSales)
                .build();
    }
}
