package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.review.dto.request.ReviewRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.exception.ReviewUnauthorizedException;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserEntityFinder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewUserService {

    private final ReviewRepository reviewRepository;
    private final OrderEntityFinder orderEntityFinder;
    private final ReviewEntityFinder reviewEntityFinder;
    private final AccountEntityFinder accountEntityFinder;
    private final UserEntityFinder userEntityFinder;

    // 메뉴에 대한 리뷰 작성
    @Transactional
    public ReviewResponse createOrderReviewResponse(Long orderId, ReviewRequest request) {
        User authUser = getUserByAuthAccount();
        Order order = orderEntityFinder.getOrderById(orderId);

        if (!order.getUser().getAccount().getId().equals(authUser.getAccount().getId())) {
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
    public ReviewResponse getOrderReviewResponse(Long reviewId) {
        User authUser = getUserByAuthAccount();
        // 1) 리뷰 존재여부 확인
        Review review = reviewEntityFinder.getReviewById(reviewId);
        validateReviewAuthorOrThrow(review, authUser);

        return ReviewResponse.from(review);
    }

    // 리뷰 수정
    @Transactional
    public ReviewResponse updateReviewResponse(ReviewRequest request, Long reviewId) {
        Review review = reviewEntityFinder.getReviewById(reviewId);
        User authUser = getUserByAuthAccount();
        validateReviewAuthorOrThrow(review, authUser);

        review.update(request.content(), request.rating());
        return ReviewResponse.from(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewEntityFinder.getReviewById(reviewId);
        User authUser = getUserByAuthAccount();
        validateReviewAuthorOrThrow(review, authUser);

        review.delete();
    }

    private void validateReviewAuthorOrThrow(Review review, User authUser) {
        Long writerAccountId = review.getUser().getAccount().getId();
        if (!writerAccountId.equals(authUser.getAccount().getId())) {
            throw new ReviewUnauthorizedException();
        }
    }

    @Transactional(readOnly = true)
    public User getUserByAuthAccount() {
        Account userAccount = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().getAccountId());
        return userEntityFinder.getUserByAccount(userAccount);
    }
}
