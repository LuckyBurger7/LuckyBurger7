package org.example.luckyburger.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.menu.enums.MenuCategory;

public record MenuRequest(
        @NotBlank(message = "메뉴 이름은 필수값입니다.")
        String name,

        @NotNull(message = "메뉴 카테고리는 필수값입니다.")
        MenuCategory menuCategory,

        @NotNull(message = "가격은 필수값입니다.")
        long price
) {

}
