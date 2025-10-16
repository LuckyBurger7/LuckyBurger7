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
public class ReviewOwnerController {

    private final ReviewService reviewService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user/orders/{orderId}/reviews")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @RequestBody ReviewCreateRequest request) {

        ReviewResponse response = reviewService.createUserReview(request);
        return ApiResponse.created(response);
    }
}
