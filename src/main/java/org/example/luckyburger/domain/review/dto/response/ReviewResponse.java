package org.example.luckyburger.domain.review.dto.response;

import java.time.LocalDateTime;
import org.example.luckyburger.domain.review.entity.Review;

public record ReviewResponse(
        Long id,
        double rating,
        String content,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime modified
) {
    public static ReviewResponse from(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getRating(),
                review.getContent(),
                review.getComment(),
                review.getCreatedAt(),
                review.getModifiedAt()
        );
    }
}
