package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OwnerSignupRequest(
        @NotBlank
        String email,

        @NotBlank
        String password,

        @NotBlank
        String name,

        @NotNull
        Long shopId
) {
}
