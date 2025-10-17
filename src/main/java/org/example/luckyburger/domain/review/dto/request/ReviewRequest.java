package org.example.luckyburger.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(

        @NotBlank
        String content,

        @Min(0) @Max(5)
        double rating
) {
}
