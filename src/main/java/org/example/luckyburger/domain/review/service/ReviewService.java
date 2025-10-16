package org.example.luckyburger.domain.review.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.CommonErrorCode;
import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderService;
import org.example.luckyburger.domain.review.dto.request.ReviewCreateRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.entity.Review;
import org.example.luckyburger.domain.review.repository.ReviewRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopService;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderService orderService;
    private final UserService userService;
    private final ShopService shopService;

    // 메뉴에 대한 리뷰 작성
    @Transactional
    public ReviewResponse createOrderReview(AuthAccount authAccount, Long orderId, ReviewCreateRequest request) {
        User user = userService.getUserById(authAccount.accountId());
        Order order = orderService.getOrderById(orderId);
        Shop shop = shopService.getShopById(request.shopId());

        Review review = Review.of(
                user,
                shop,
                order,
                request.content(),
                request.rating(),
                request.comment()
        );
        Review saved = reviewRepository.save(review);

        return ReviewResponse.from(saved);
    }

    // 작성한 리뷰 단일 조회
    @Transactional(readOnly = true)
    public ReviewResponse getOrderReview(Long reviewId) {
        // 1) 리뷰 존재여부 확인
        Review review = reviewRepository.findById(reviewId).orElseThrow(
                () -> new GlobalException(CommonErrorCode.INVALID_REVIEW));

        return ReviewResponse.from(review);
    }

    // 내가 작성한 리뷰 전체 조회
    @Transactional(readOnly = true)
    public Page<ReviewResponse> getAllReview(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findReviewPage(pageable);

        return reviews.map(ReviewResponse::from);
    }
}
