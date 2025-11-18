package org.example.luckyburger.domain.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.shop.dto.request.CouponPolicyRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopMenuRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.CouponPolicyResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopDashboardResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopTotalSalesResponse;
import org.example.luckyburger.domain.shop.service.ShopOwnerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @Operation(summary = "[점포]쿠폰 사용 여부 수정")
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
    @Operation(summary = "[점포]쿠폰 조회")
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
    @Operation(summary = "[점포] 상태 변경")
    @PutMapping("/v1/owner/shops/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShopStatus(@PathVariable Long shopId,
                                                                      @RequestBody ShopUpdateRequest request) {

        return ApiResponse.success(shopOwnerService.updateShopStatus(shopId, request));
    }

    /**
     * 점포 메뉴 조회
     *
     * @param shopId
     * @return
     */
    @Operation(summary = "[점포]메뉴 조회")
    @GetMapping("v1/owner/shops/{shopId}/menus")
    public ResponseEntity<ApiPageResponse<ShopMenuResponse>> getAllShopMenu(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return ApiPageResponse.success(shopOwnerService.getAllShopMenuResponse(shopId, pageable));
    }

    /**
     * 점포 메뉴 상태 변경
     *
     * @param shopId
     * @param menuId
     * @param request
     * @return
     */
    @Operation(summary = "[점포]메뉴 상태 변경")
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
    @Operation(summary = "[점포]월 정산")
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
    @Operation(summary = "[점포]데시보드")
    @GetMapping("/v1/owner/shops/{shopId}/dashboard")
    public ResponseEntity<ApiResponse<ShopDashboardResponse>> getDashboardByShopId(@PathVariable Long shopId) {
        return ApiResponse.success(shopOwnerService.getShopDashboardByShopId(shopId));
    }
}
