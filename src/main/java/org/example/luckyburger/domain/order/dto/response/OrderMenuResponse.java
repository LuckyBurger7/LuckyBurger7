package org.example.luckyburger.domain.order.dto.response;

import lombok.Builder;

@Builder
public record OrderMenuResponse(
        Long menuId,
        String name,
        long unitPrice,
        int quantity
) {
    public static OrderMenuResponse of(Long menuId, String menuName, long unitPrice, int quantity) {
        return OrderMenuResponse.builder()
                .menuId(menuId)
                .name(menuName)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .build();
    }
}


