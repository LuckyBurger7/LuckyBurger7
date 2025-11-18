package org.example.luckyburger.domain.coupon.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured(AccountRole.Authority.ADMIN)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponAdminController {

    private final CouponAdminService couponAdminService;

    @Operation(summary = "쿠폰 추가")
    @PostMapping("/v2/admin/coupons")
    public ResponseEntity<ApiResponse<CouponResponse>> createCouponWithRedis(
            @Valid @RequestBody CouponRequest couponRequest) {
        return ApiResponse.created(couponAdminService.createCouponWithRedis(couponRequest));
    }

    @Operation(summary = "쿠폰 변경")
    @PutMapping("/v2/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCouponWithRedis(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponRequest couponRequest) {
        return ApiResponse.success(couponAdminService.updateCouponWithRedis(couponId, couponRequest));
    }

    @Operation(summary = "쿠폰 삭제")
    @DeleteMapping("/v2/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> deleteCouponWithRedis(@PathVariable Long couponId) {
        couponAdminService.deleteCouponWithRedis(couponId);
        return ApiResponse.noContent();
    }

    /*
    @PostMapping("/v1/admin/coupons")
    public ResponseEntity<ApiResponse<CouponResponse>> createCoupon(@Valid @RequestBody CouponRequest couponRequest) {
        return ApiResponse.created(couponAdminService.createCoupon(couponRequest));
    }
     */

    /*
    @PutMapping("/v1/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> updateCoupon(
            @PathVariable Long couponId,
            @Valid @RequestBody CouponRequest couponRequest) {
        return ApiResponse.success(couponAdminService.updateCoupon(couponId, couponRequest));
    }
     */

    /*
    @DeleteMapping("/v1/admin/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponResponse>> deleteCoupon(@PathVariable Long couponId) {
        couponAdminService.deleteCoupon(couponId);
        return ApiResponse.noContent();
    }
     */

    @Operation(summary = "활성화 상태의 쿠폰 확인")
    @GetMapping("/v1/admin/coupons/availability")
    public ResponseEntity<ApiPageResponse<CouponResponse>> getAllCouponsByAvailable(
            @PageableDefault Pageable pageable) {
        return ApiPageResponse.success(couponAdminService.getAllAvailableCoupon(pageable));
    }
}
