package org.example.luckyburger.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.coupon.dto.response.UserCouponResponse;
import org.example.luckyburger.domain.coupon.service.CouponUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured(AccountRole.Authority.USER)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponUserController {

    private final CouponUserService couponUserService;

    @PostMapping("/v1/user/coupons/{couponId}")
    public ResponseEntity<ApiResponse<UserCouponResponse>> issueCoupon(@PathVariable Long couponId) {
        return ApiResponse.success(couponUserService.issueCoupon(couponId));
    }

    @GetMapping("/v1/user/coupons")
    public ResponseEntity<ApiPageResponse<UserCouponResponse>> getAllVerifiedUserCoupon(
            @PageableDefault Pageable pageable
    ) {
        return ApiPageResponse.success(couponUserService.getAllVerifiedUserCouponResponse(pageable));
    }
}
