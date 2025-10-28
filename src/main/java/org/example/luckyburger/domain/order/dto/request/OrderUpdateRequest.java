package org.example.luckyburger.domain.order.dto.request;


import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.order.enums.OrderStatus;

public record OrderUpdateRequest(
        @NotNull(message = "주문 상태는 필수값입니다.")
        OrderStatus status
) {
}
