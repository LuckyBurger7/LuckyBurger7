package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record OwnerUpdateRequest(
        @NotNull(message = "가게 ID는 필수값입니다.")
        Long shopId
) {
}
