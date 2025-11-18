package org.example.luckyburger.domain.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.coupon.dto.response.UserCouponResponse;
import org.example.luckyburger.domain.coupon.service.CouponIssuePessimisticService;
import org.example.luckyburger.domain.coupon.service.CouponUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured(AccountRole.Authority.USER)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponUserController {

    private final CouponUserService couponUserService;
    private final CouponIssuePessimisticService couponIssuePessimisticService;

    /*
    @PostMapping("/v1/user/coupons/{couponId}")
    public ResponseEntity<ApiResponse<UserCouponResponse>> issueCoupon(@PathVariable Long couponId) {
        return ApiResponse.success(couponUserService.issueCoupon(couponId));
    }
     */

    @Operation(summary = "쿠폰 발급")
    @PostMapping("/v2/user/coupons/{couponId}")
    public ResponseEntity<ApiResponse<UserCouponResponse>> issueCouponWithRedis(@PathVariable Long couponId) {
        return ApiResponse.success(couponUserService.issueCouponWithRedis(couponId));
    }

    /* @PostMapping("/v3/user/coupons/{couponId}")
    public ResponseEntity<ApiResponse<UserCouponResponse>> issueCouponWithPessimistic(@PathVariable Long couponId) {
        return ApiResponse.success(couponIssuePessimisticService.issueCouponWithPessimistic(couponId));
    }*/

    @Operation(summary = "발급 받은 쿠폰 확인")
    @GetMapping("/v1/user/coupons")
    public ResponseEntity<ApiPageResponse<UserCouponResponse>> getAllVerifiedUserCoupon(
            @PageableDefault Pageable pageable
    ) {
        return ApiPageResponse.success(couponUserService.getAllVerifiedUserCouponResponse(pageable));
    }
}
