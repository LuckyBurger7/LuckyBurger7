package org.example.luckyburger.domain.coupon.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.coupon.dto.request.CouponRequest;
import org.example.luckyburger.domain.coupon.dto.response.CouponResponse;
import org.example.luckyburger.domain.coupon.service.CouponAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured(AccountRole.Authority.ADMIN)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @PostMapping("/v1/admin/coupons")
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(@Valid @RequestBody CouponRequest couponRequest) {
        return ApiResponse.created(couponAdminService.createCoupon(couponRequest));
    }

    @PutMapping("/v1/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponRequest couponRequest) {
        return ApiResponse.success(couponAdminService.updateCoupon(couponId, couponRequest));
    }

    @DeleteMapping("/v1/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);
        return ApiResponse.noContent();
    }

    @GetMapping("/v1/admin/coupons/availability")
    public ResponseEntity<ApiPageResponse<CouponResponse>> getAllCouponsByAvailable(
            @PageableDefault Pageable pageable) {
        return ApiPageResponse.success(couponAdminService.getAllAvailableCoupon(pageable));
    }
}
