package org.example.luckyburger.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartDeleteMenuRequest(
        @NotNull
        Long cartMenuId
) {

}
