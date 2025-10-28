package org.example.luckyburger.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CartUpdateMenuRequest(
        @NotNull(message = "장바구니 메뉴 ID는 필수값입니다.")
        Long cartMenuId,

        @NotNull(message = "수량은 필수값이어야 합니다.")
        @Min(value = 1, message = "수량은 1 이상의 값이어야 합니다.")
        int quantity
) {
}
