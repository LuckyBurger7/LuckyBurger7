package org.example.luckyburger.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record UserSignupRequest(
        @NotBlank(message = "이메일은 필수값입니다.")
        String email,

        @NotBlank(message = "비밀번호는 필수값입니다.")
        String password,

        @NotBlank(message = "이름은 필수값입니다.")
        String name,

        @NotBlank(message = "전화번호는 필수값입니다.")
        @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$", message = "올바른 전화번호 형식이 아닙니다.")
        String phone,

        @NotBlank(message = "주소는 필수값입니다.")
        String address,

        @NotBlank(message = "상세 주소는 필수값입니다.")
        String street
) {
}
