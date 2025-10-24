package org.example.luckyburger.domain.shop.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.shop.dto.request.CouponPolicyRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopMenuRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.*;
import org.example.luckyburger.domain.shop.service.ShopOwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Secured(AccountRole.Authority.OWNER)
public class ShopOwnerController {

    private final ShopOwnerService shopOwnerService;

    /**
     * 점포별 쿠폰 사용 여부 수정
     *
     * @param shopId
     * @param couponId
     * @return
     */
    @PutMapping("/v1/owner/shops/{shopId}/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponPolicyResponse>> couponAvailabilityByShop(
            @PathVariable Long shopId,
            @PathVariable Long couponId,
            @Valid @RequestBody CouponPolicyRequest cpr
    ) {
        return ApiResponse.success(shopOwnerService.updateCouponStatus(shopId, couponId, cpr));
    }

    /**
     * 점포 쿠폰 조회
     *
     * @param couponId
     * @param shopId
     * @return
     */
    @GetMapping("/v1/owner/shops/{shopId}/coupons/{couponId}")
    public ResponseEntity<ApiResponse<CouponPolicyResponse>> getCouponPolicyByShop(
            @PathVariable Long couponId,
            @PathVariable Long shopId
    ) {
        return ApiResponse.success(shopOwnerService.getCouponPolicyResponse(shopId, couponId));
    }


    /**
     * 점포 상태 변경
     *
     * @param shopId
     * @param request
     * @return
     */
    @PutMapping("/v1/owner/shops/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShopStatus(@PathVariable Long shopId,
                                                                      @RequestBody ShopUpdateRequest request) {

        return ApiResponse.success(shopOwnerService.updateShopStatus(shopId, request));
    }

    /**
     * 점포 메뉴 상태 변경
     *
     * @param shopId
     * @param menuId
     * @param request
     * @return
     */
    @PutMapping("/v1/owner/shops/{shopId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<ShopMenuResponse>> updateMenuStatus(
            @PathVariable Long shopId,
            @PathVariable Long menuId,
            @Valid @RequestBody ShopMenuRequest request) {
        return ApiResponse.success(shopOwnerService.updateMenuStatus(shopId, menuId, request));
    }

    /**
     * 월 정산
     *
     * @param shopId
     * @param month
     * @return
     */
    @GetMapping("/v1/owner/shops/{shopId}/sales/monthly")
    public ResponseEntity<ApiResponse<ShopTotalSalesResponse>> getTotalSalesByShopId(
            @PathVariable Long shopId,
            @RequestParam(value = "month", required = false) Integer month
    ) {
        var res = shopOwnerService.getTotalSalesByShopIdAndMonth(shopId, month);
        return ApiResponse.success(res);
    }

    /**
     * 점포 대시보드
     *
     * @param shopId 점포 아이디
     * @return 대시보드 응답 DTO
     */
    @GetMapping("/v1/owner/shops/{shopId}/dashboard")
    public ResponseEntity<ApiResponse<ShopDashboardResponse>> getDashboardByShopId(@PathVariable Long shopId) {
        return ApiResponse.success(shopOwnerService.getShopDashboardByShopId(shopId));
    }
}
