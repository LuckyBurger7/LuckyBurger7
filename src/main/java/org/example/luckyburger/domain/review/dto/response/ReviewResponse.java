package org.example.luckyburger.domain.review.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.review.entity.Review;

import java.time.LocalDateTime;

@Builder
public record ReviewResponse(
        Long reviewId,
        double rating,
        String content,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .reviewId(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build();
    }
}
