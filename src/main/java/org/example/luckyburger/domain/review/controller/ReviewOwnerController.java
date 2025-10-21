package org.example.luckyburger.domain.review.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole.Authority;
import org.example.luckyburger.domain.review.dto.request.CommentRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.service.ReviewOwnerService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
@Secured(Authority.OWNER)
public class ReviewOwnerController {

    private final ReviewOwnerService reviewOwnerService;

    // 주문에 대한 리뷰를 전체 불러오기
    @GetMapping("/v1/owner/shops/{shopId}/reviews")
    public ResponseEntity<ApiPageResponse<ReviewResponse>> getShopReviews(
            @PageableDefault Pageable pageable,
            @PathVariable Long shopId) {
        return ApiPageResponse.success(reviewOwnerService.getShopReviewsResponse(pageable, shopId));
    }

    // 리뷰에 대한 댓글 작성
    @PostMapping("/v1/owner/reviews/{reviewId}/comments")
    public ResponseEntity<ApiResponse<Void>> createComment(
            @PathVariable Long reviewId,
            @Valid @RequestBody CommentRequest request) {
        reviewOwnerService.createComment(reviewId, request);
        return ApiResponse.noContent();
    }
}
