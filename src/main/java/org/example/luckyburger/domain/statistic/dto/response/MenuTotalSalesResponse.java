package org.example.luckyburger.domain.statistic.dto.response;

import lombok.Builder;

@Builder
public record MenuTotalSalesResponse(
        Long menuId,
        String menuName,
        Long totalSales
) {
    public static MenuTotalSalesResponse of(
            Long menuId,
            String menuName,
            Long totalSales
    ) {
        return MenuTotalSalesResponse.builder()
                .menuId(menuId)
                .menuName(menuName)
                .totalSales(totalSales)
                .build();
    }
}
