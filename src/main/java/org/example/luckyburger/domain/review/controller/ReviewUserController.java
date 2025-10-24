package org.example.luckyburger.domain.review.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.review.dto.request.ReviewRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.service.ReviewUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewUserController {

    private final ReviewUserService reviewUserService;

    // 주문에 대한 리뷰 생성
    @PostMapping("/v1/user/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createOrderReview(
            @PathVariable Long orderId,
            @RequestBody ReviewRequest request) {

        return ApiResponse.created(reviewUserService.createOrderReviewResponse(orderId, request));
    }

    // 작성한 리뷰 단일 조회
    @GetMapping("/v1/user/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getOrderReview(
            @PathVariable Long reviewId) {

        return ApiResponse.success(reviewUserService.getOrderReviewResponse(reviewId));
    }

    // 주문에 대한 리뷰 수정
    @PutMapping("/v1/user/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @Valid @RequestBody ReviewRequest request,
            @PathVariable Long reviewId) {

        return ApiResponse.success(reviewUserService.updateReviewResponse(request, reviewId));
    }

    // 작성한 리뷰 삭제
    @DeleteMapping("/v1/user/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId) {

        reviewUserService.deleteReview(reviewId);
        return ApiResponse.noContent();
    }
}
