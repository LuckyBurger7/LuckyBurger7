package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountSignupRequest(
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name
) {
}
