package org.example.luckyburger.domain.statistic.dto.response;

import lombok.Builder;

@Builder
public record MonthTotalSalesResponse(
        Integer year,
        Integer month,
        Long totalSales
) {
    public static MonthTotalSalesResponse of(Integer year, Integer month, Long totalSales) {
        return MonthTotalSalesResponse.builder().year(year).month(month).totalSales(totalSales).build();
    }
}
