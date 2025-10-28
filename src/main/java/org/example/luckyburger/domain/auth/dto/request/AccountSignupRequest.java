package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountSignupRequest(
        @NotBlank(message = "이메일은 필수값입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수값입니다.")
        String password,

        @NotBlank(message = "이름은 필수값입니다.")
        String name
) {
}
