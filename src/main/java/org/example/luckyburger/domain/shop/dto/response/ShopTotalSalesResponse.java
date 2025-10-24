package org.example.luckyburger.domain.shop.dto.response;

public record ShopTotalSalesResponse(
        Long shopId,
        Long totalSales
) {
}