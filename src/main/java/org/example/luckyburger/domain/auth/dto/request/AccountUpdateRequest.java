package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AccountUpdateRequest(
        @NotBlank
        String name
) {
}
