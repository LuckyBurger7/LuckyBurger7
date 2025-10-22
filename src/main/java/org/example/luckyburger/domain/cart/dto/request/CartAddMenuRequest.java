package org.example.luckyburger.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartAddMenuRequest(
        @NotNull
        Long shopMenuId
) {
}
