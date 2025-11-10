package org.example.luckyburger.domain.order.dto.cache;

import lombok.*;
import org.example.luckyburger.domain.cart.entity.CartMenu;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFormCache {

    private Long shopMenuId;
    private int quantity;
    private long price;

    public static OrderFormCache from(CartMenu cartMenu) {
        return OrderFormCache.builder()
                .shopMenuId(cartMenu.getShopMenu().getId())
                .quantity(cartMenu.getQuantity())
                .price(cartMenu.getShopMenu().getMenu().getPrice())
                .build();
    }

}
