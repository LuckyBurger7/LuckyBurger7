package org.example.luckyburger.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.review.dto.request.ReviewCreateRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.service.ReviewService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class ReviewUserController {

    private final ReviewService reviewService;

    // 주문에 대한 리뷰 생성
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createOrderReview(
            @AuthenticationPrincipal AuthAccount authAccount,
            @PathVariable Long orderId,
            @RequestBody ReviewCreateRequest request) {

        ReviewResponse response = reviewService.createOrderReview(authAccount, orderId, request);
        return ApiResponse.created(response);
    }

    // 작성한 리뷰 단일 조회
    @GetMapping("/user/orders/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getOrderReview(
            @PathVariable Long reviewId) {

        return ApiResponse.success(reviewService.getOrderReview(reviewId));
    }

    // 내가 작성한 리뷰 전체 조회
    @GetMapping("/user/reviews")
    public ResponseEntity<ApiPageResponse<ReviewResponse>> getAllReview(@PageableDefault Pageable pageable) {
        return ApiPageResponse.success(reviewService.getAllReview(pageable));
    }
}
