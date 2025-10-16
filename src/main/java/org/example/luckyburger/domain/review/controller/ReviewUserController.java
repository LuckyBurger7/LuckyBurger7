package org.example.luckyburger.domain.review.controller;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.review.dto.request.ReviewCreateRequest;
import org.example.luckyburger.domain.review.dto.response.ReviewResponse;
import org.example.luckyburger.domain.review.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // 추 후 로그인에 대한 인증 추가 예정
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createOrderReview(
            @RequestBody ReviewCreateRequest request) {

        ReviewResponse response = reviewService.createOrderReview(request);
        return ApiResponse.created(response);
    }
}
