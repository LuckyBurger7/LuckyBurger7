package org.example.luckyburger.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(

        @NotBlank(message = "댓글 내용은 필수값입니다.")
        String comment
) {
}
