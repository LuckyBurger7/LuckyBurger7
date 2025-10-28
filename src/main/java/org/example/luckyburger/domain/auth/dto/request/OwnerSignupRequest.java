package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OwnerSignupRequest(
        @NotBlank(message = "이메일은 필수값입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수값입니다.")
        String password,

        @NotBlank(message = "이름은 필수값입니다.")
        String name,

        @NotNull(message = "가게 ID는 필수값입니다.")
        Long shopId
) {
}
