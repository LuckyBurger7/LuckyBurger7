package org.example.luckyburger.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(

        @NotBlank
        String content,

        @NotNull
        double rating,

        String comment
) {
}
