package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountUpdateRequest(
        @NotBlank
        String name
) {
}
