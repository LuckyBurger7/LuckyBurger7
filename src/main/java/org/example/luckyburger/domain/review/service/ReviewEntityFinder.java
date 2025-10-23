package org.example.luckyburger.domain.review.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.ReviewNotFoundException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class ReviewEntityFinder {

    private final ReviewRepository reviewRepository;

    public Review getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                ReviewNotFoundException::new);
        if (review.getDeletedAt() != null) {
            throw new ReviewNotFoundException();
        }
        return review;
    }

    public List<Review> getReviewListByShop(Shop shop) {
        return reviewRepository.findAllByShop(shop);
    }

    public Double getSumOfRatingByShop(Shop shop) {
        return reviewRepository.findSumOfRatingByShop(shop);
    }

    public Long getCountByShop(Shop shop) {
        return reviewRepository.countByShop(shop);
    }
}
