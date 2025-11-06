package org.example.luckyburger.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record CartDeleteMenuRequest(
        @NotNull(message = "점포 메뉴 ID는 필수값입니다.")
        Long shopMenuId
) {

}
