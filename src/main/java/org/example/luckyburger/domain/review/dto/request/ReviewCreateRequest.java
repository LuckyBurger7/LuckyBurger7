package org.example.luckyburger.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReviewCreateRequest(

        @NotNull
        Long shopId,

        @NotBlank
        String content,

        @NotNull
        double rating,

        String comment
) {
}
