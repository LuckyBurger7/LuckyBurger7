package org.example.luckyburger.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.review.dto.request.ReviewRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.service.ReviewUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewUserController {

    private final ReviewUserService reviewUserService;

    // 주문에 대한 리뷰 생성
    @PostMapping("/v1/user/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createOrderReview(
            @AuthenticationPrincipal AuthAccount authAccount,
            @PathVariable Long orderId,
            @RequestBody ReviewRequest request) {

        return ApiResponse.created(reviewUserService.createOrderReview(authAccount, orderId, request));
    }

    // 작성한 리뷰 단일 조회
    @GetMapping("/v1/user/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> getOrderReview(
            @AuthenticationPrincipal AuthAccount authAccount,
            @PathVariable Long reviewId) {

        return ApiResponse.success(reviewUserService.getOrderReview(reviewId, authAccount));
    }

    // 주문에 대한 리뷰 수정
    @PutMapping("/v1/user/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @AuthenticationPrincipal AuthAccount authAccount,
            @Valid @RequestBody ReviewRequest request,
            @PathVariable Long reviewId) {
        
        return ApiResponse.success(reviewUserService.updateReview(request, reviewId, authAccount));
    }

    // 작성한 리뷰 삭제
    @DeleteMapping("/v1/user/reviews/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @PathVariable Long reviewId,
            @AuthenticationPrincipal AuthAccount authAccount) {

        reviewUserService.deleteReview(reviewId, authAccount);
        return ApiResponse.noContent();
    }
}
