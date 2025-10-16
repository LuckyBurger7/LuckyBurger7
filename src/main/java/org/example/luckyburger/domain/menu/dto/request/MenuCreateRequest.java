package org.example.luckyburger.domain.menu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.menu.enums.MenuCategory;

public record MenuCreateRequest(

        @NotBlank
        String name,

        @NotNull
        MenuCategory menuCategory,

        @NotNull
        long price
) {

}
