package org.example.luckyburger.domain.review.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record ReviewRequest(

        @NotBlank(message = "리뷰 내용은 필수값입니다.")
        String content,

        @Min(value = 0, message = "평점은 0 이상이어야 합니다.")
        @Max(value = 5, message = "평점은 5 이하여야 합니다.")
        double rating
) {
}
