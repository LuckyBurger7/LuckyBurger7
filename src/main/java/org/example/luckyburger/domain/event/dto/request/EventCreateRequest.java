package org.example.luckyburger.domain.event.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EventCreateRequest(

        @NotBlank(message = "제목은 필수값입니다.")
        String title,

        @NotBlank(message = "내용은 필수값입니다.")
        String description
) {
}
