package org.example.luckyburger.domain.cart.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.cart.entity.CartMenu;

@Builder(access = AccessLevel.PRIVATE)
public record CartMenuResponse(
        Long shopMenuId,
        String menuName,
        String shopName,
        int quantity,
        long price
) {
    public static CartMenuResponse of(
            Long shopMenuId,
            String menuName,
            String shopName,
            int quantity,
            long price) {
        return CartMenuResponse.builder()
                .shopMenuId(shopMenuId)
                .menuName(menuName)
                .shopName(shopName)
                .quantity(quantity)
                .price(price)
                .build();
    }

    public static CartMenuResponse from(CartMenu cartMenu) {
        return CartMenuResponse.builder()
                .shopMenuId(cartMenu.getShopMenu().getId())
                .menuName(cartMenu.getShopMenu().getMenu().getName())
                .shopName(cartMenu.getShopMenu().getShop().getName())
                .quantity(cartMenu.getQuantity())
                .price(cartMenu.getShopMenu().getMenu().getPrice())
                .build();
    }
}
