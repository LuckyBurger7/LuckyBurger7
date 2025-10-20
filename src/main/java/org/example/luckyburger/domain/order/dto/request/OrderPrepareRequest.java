package org.example.luckyburger.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;

public record OrderPrepareRequest(
        @NotNull
        Long shopId,
        @NotNull
        Long cartId
) {
}


