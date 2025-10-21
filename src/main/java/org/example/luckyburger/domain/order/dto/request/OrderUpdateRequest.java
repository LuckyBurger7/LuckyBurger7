package org.example.luckyburger.domain.order.dto.request;


import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.order.enums.OrderStatus;

public record OrderUpdateRequest(
        @NotNull
        OrderStatus status
) {
}
