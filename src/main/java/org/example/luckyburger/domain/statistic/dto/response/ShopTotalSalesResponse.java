package org.example.luckyburger.domain.statistic.dto.response;

import lombok.Builder;

@Builder
public record ShopTotalSalesResponse(
        Long shopId,
        String shopName,
        Long totalSales
) {
    public static ShopTotalSalesResponse of(
            Long shopId,
            String shopName,
            Long totalSales
    ) {
        return ShopTotalSalesResponse.builder()
                .shopId(shopId)
                .shopName(shopName)
                .totalSales(totalSales).build();
    }
}
