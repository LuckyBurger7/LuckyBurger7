package org.example.luckyburger.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(

        @NotBlank
        String comment
) {
}
