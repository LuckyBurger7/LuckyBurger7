package org.example.luckyburger.domain.menu.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;

@Builder
public record MenuResponse(Long menuId, String name, MenuCategory menuCategory, long price) {

    public static MenuResponse from(Menu menu) {
        return MenuResponse.builder()
                .menuId(menu.getId())
                .name(menu.getName())
                .menuCategory(menu.getCategory())
                .price(menu.getPrice())
                .build();
    }
}