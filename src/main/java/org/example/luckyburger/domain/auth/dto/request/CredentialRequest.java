package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CredentialRequest(
        @NotBlank(message = "비밀번호는 필수값입니다.")
        String password
) {
}
