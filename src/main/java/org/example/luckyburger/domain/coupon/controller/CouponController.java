package org.example.luckyburger.domain.coupon.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.coupon.dto.response.CouponResponse;
import org.example.luckyburger.domain.coupon.service.CouponService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/v1/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> getCouponResponse(@PathVariable Long couponId) {
        return ApiResponse.success(couponService.getCouponResponse(couponId));
    }

    @GetMapping("/v1/coupons")
    public ResponseEntity<ApiPageResponse<CouponResponse>> getAllCouponResponse(@PageableDefault Pageable pageable) {
        return ApiPageResponse.success(couponService.getAllCouponResponse(pageable));
    }
}
