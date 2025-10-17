package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CredentialRequest(
        @NotBlank
        String password
) {
}
