package org.example.luckyburger.domain.event.dto.request;

import jakarta.validation.constraints.NotBlank;

public record EventCreateRequest(

        @NotBlank
        String title,

        @NotBlank
        String description
) {
}
