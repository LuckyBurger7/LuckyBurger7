package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;

public record OwnerUpdateRequest(
        @NotNull
        Long shopId
) {
}
