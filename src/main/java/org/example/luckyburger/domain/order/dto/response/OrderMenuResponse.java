package org.example.luckyburger.domain.order.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.cart.entity.CartMenu;
import org.example.luckyburger.domain.order.entity.OrderForm;
import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.example.luckyburger.domain.shop.entity.ShopMenu;

@Builder
public record OrderMenuResponse(
        Long menuId,
        String name,
        long unitPrice,
        int quantity
) {
    private static OrderMenuResponse of(ShopMenu shopMenu, int quantity) {
        return OrderMenuResponse.builder()
                .menuId(shopMenu.getId())
                .name(shopMenu.getMenu().getName())
                .unitPrice(shopMenu.getMenu().getPrice())
                .quantity(quantity)
                .build();
    }

    public static OrderMenuResponse from(OrderForm orderForm) {
        return of(orderForm.getShopMenu(), orderForm.getQuantity());
    }

    public static OrderMenuResponse from(CartMenu cartMenu) {
        return of(cartMenu.getShopMenu(), cartMenu.getQuantity());
    }

    public static OrderMenuResponse from(OrderMenu orderMenu) {
        return of(orderMenu.getShopMenu(), orderMenu.getQuantity());
    }
}


