package org.example.luckyburger.domain.menu.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;

@Builder
public record MenuCreateResponse(Long id, String name, MenuCategory menuCategory, long price) {

    public static MenuCreateResponse from(Menu menu) {
        return MenuCreateResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .menuCategory(menu.getCategory())
                .price(menu.getPrice())
                .build();
    }
}