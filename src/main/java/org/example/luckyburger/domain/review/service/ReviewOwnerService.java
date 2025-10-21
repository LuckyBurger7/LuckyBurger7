package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.review.dto.request.CommentRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.CommentAlreadyExistsException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewOwnerService {

    private final ReviewRepository reviewRepository;
    private final ReviewEntityFinder reviewEntityFinder;

    @Transactional(readOnly = true)
    public Page<ReviewResponse> getShopReviews(Pageable pageable, Long shopId) {

        Pageable limited = PageRequest.of(
                Math.max(0, pageable.getPageNumber()),
                Math.min(pageable.getPageSize(), 3),
                pageable.getSort()
        );
        Page<Review> reviews = reviewRepository.findShopReviews(shopId, limited);
        return reviews.map(ReviewResponse::from);
    }

    @Transactional
    public ReviewResponse createComment(Long reviewId, CommentRequest request) {
        Review review = reviewEntityFinder.getReviewById(reviewId);

        if (review.getComment() != null && !review.getComment().isEmpty()) {
            throw new CommentAlreadyExistsException();
        }
        review.writeComment(request.comment());
        return ReviewResponse.from(review);
    }
}
