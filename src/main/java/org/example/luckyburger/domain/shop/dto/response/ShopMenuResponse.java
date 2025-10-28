package org.example.luckyburger.domain.shop.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;

@Builder
public record ShopMenuResponse(Long shopMenuId, String name, MenuCategory menuCategory, long price,
                               ShopMenuStatus menuStatus) {

    public static ShopMenuResponse from(ShopMenu shopMenu) {
        return ShopMenuResponse.builder()
                .shopMenuId(shopMenu.getId())
                .name(shopMenu.getMenu().getName())
                .menuCategory(shopMenu.getMenu().getCategory())
                .price(shopMenu.getMenu().getPrice())
                .menuStatus(shopMenu.getStatus())
                .build();
    }
}
