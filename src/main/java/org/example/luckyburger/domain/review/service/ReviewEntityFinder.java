package org.example.luckyburger.domain.review.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.NotFoundReviewException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntityFinder {

    private final ReviewRepository reviewRepository;

    @Transactional(readOnly = true)
    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                NotFoundReviewException::new);
    }
}
