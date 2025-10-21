package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.dto.request.ReviewRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.ReviewUnauthorizedException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewUserService {

    private final ReviewRepository reviewRepository;
    private final OrderEntityFinder orderEntityFinder;
    private final ReviewEntityFinder reviewEntityFinder;

    // 메뉴에 대한 리뷰 작성
    @Transactional
    public ReviewResponse createOrderReviewResponse(AuthAccount authAccount, Long orderId, ReviewRequest request) {
        Order order = orderEntityFinder.getOrderById(orderId);

        if (!order.getUser().getAccount().getId().equals(authAccount.getAccountId())) {
            throw new ReviewUnauthorizedException();
        }

        Review review = Review.of(
                order.getUser(),
                order.getShop(),
                order,
                request.content(),
                request.rating()
        );
        Review saved = reviewRepository.save(review);
        return ReviewResponse.from(saved);
    }

    // 작성한 리뷰 단일 조회
    @Transactional(readOnly = true)
    public ReviewResponse getOrderReviewResponse(Long reviewId, AuthAccount authAccount) {
        // 1) 리뷰 존재여부 확인
        Review review = reviewEntityFinder.getReviewById(reviewId);
        validateReviewAuthorOrThrow(review, authAccount);
        return ReviewResponse.from(review);
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponse updateReviewResponse(ReviewRequest request, Long reviewId, AuthAccount authAccount) {
        Review review = reviewEntityFinder.getReviewById(reviewId);
        validateReviewAuthorOrThrow(review, authAccount);

        review.update(request.content(), request.rating());
        return ReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(Long reviewId, AuthAccount authAccount) {
        Review review = reviewEntityFinder.getReviewById(reviewId);
        validateReviewAuthorOrThrow(review, authAccount);
        review.delete();
    }

    private void validateReviewAuthorOrThrow(Review review, AuthAccount auth) {
        Long writerAccountId = review.getUser().getAccount().getId();
        if (!writerAccountId.equals(auth.getAccountId())) {
            throw new ReviewUnauthorizedException();
        }
    }
}
