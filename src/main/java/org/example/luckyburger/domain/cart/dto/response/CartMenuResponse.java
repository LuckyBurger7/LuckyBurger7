package org.example.luckyburger.domain.cart.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.cart.entity.CartMenu;

@Builder(access = AccessLevel.PRIVATE)
public record CartMenuResponse(
        Long cartId,
        Long cartMenuId,
        Long shopMenuId,
        String menuName,
        String shopName,
        int quantity,
        long price
) {
    public static CartMenuResponse from(CartMenu cartMenu) {
        return CartMenuResponse.builder()
                .cartId(cartMenu.getCart().getId())
                .cartMenuId(cartMenu.getId())
                .shopMenuId(cartMenu.getShopMenu().getId())
                .menuName(cartMenu.getShopMenu().getMenu().getName())
                .shopName(cartMenu.getShopMenu().getShop().getName())
                .quantity(cartMenu.getQuantity())
                .price(cartMenu.getShopMenu().getMenu().getPrice())
                .build();
    }
}
