package org.example.luckyburger.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartUpdateMenuRequest(
        @NotNull
        Long cartMenuId,

        @NotNull @Min(1)
        int quantity
) {
}
