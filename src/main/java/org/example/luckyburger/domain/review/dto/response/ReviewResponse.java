package org.example.luckyburger.domain.review.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.example.luckyburger.domain.review.entity.Review;

@Builder
public record ReviewResponse(
        Long id,
        double rating,
        String content,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime modified
) {
    public static ReviewResponse from(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .content(review.getContent())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .modified(review.getModifiedAt())
                .build();
    }
}
