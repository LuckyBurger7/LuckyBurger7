package org.example.luckyburger.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;

public record ShopUpdateRequest(
        @NotNull(message = "가게 상태는 필수값입니다.")
        BusinessStatus businessStatus
) {
}
