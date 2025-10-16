package org.example.luckyburger.domain.review.dto.request;

public record ReviewCreateRequest(

        String content,
        double rating,
        String comment
) {
}
