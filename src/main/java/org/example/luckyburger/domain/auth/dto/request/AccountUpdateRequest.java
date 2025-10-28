package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AccountUpdateRequest(
        @NotBlank(message = "이름은 필수값입니다.")
        String name
) {
}
