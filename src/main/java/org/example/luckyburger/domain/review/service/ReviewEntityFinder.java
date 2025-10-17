package org.example.luckyburger.domain.review.service;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.NotFoundReviewException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewEntityFinder {

    private final ReviewRepository reviewRepository;

    public Review getReview(Long reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                NotFoundReviewException::new);
    }

    public List<Review> getAllReviewsByShopId(Long shopId) {
        return reviewRepository.findAllByShopId(shopId);
    }
}
