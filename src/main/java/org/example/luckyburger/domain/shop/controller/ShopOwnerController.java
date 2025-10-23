package org.example.luckyburger.domain.shop.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopDashboardResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopMenuService;
import org.example.luckyburger.domain.shop.dto.request.CouponPolicyRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopMenuRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.CouponPolicyResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopTotalSalesResponse;
import org.example.luckyburger.domain.shop.service.ShopOwnerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Secured(AccountRole.Authority.OWNER)
public class ShopOwnerController {

    private final ShopMenuService shopMenuService;
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
    public ResponseEntity<ApiResponse<Shop>> changeShopStatus(@PathVariable Long shopId,
                                                              @RequestBody BusinessStatus shopStatus) {

        Shop shop = shopOwnerService.updateStatus(shopId, shopStatus);

        return ApiResponse.success(shop);
    }

    //월 정산 조회
    @GetMapping("/v1/owner/shops/{shopId}/sales/monthly")
    public ResponseEntity<ApiResponse<Integer>> getTotalSaleByMonthWithShopId(@PathVariable Long shopId,
                                                                              @RequestParam LocalDate date) {

        int totalSaleByMonthWithShopId = shopOwnerService.getTotalSaleByMonthWithShopId(shopId, date);

        return ApiResponse.success(totalSaleByMonthWithShopId);

    }

    @GetMapping("/v1/owner/shops/{shopId}/orders/daily")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getOrderTodayByShop(@RequestParam LocalDate date,
                                                                              @PathVariable Long shopId,
                                                                              @RequestParam int page,
                                                                              @RequestParam int size) {

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        Page<OrderResponse> orderTodayByShop = shopOwnerService.getOrderTodayByShop(start, end, shopId, page, size);

        return ApiPageResponse.success(orderTodayByShop);

    }

    //shopId는 접속된 로그인에서 추출해서 사용하도록 변경
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
    public ResponseEntity<ApiResponse<Menu>> updateMenuStatusByOwner(@PathVariable Long shopId,
                                                                     @PathVariable Long menuId,
                                                                     @RequestBody ShopMenuStatus shopMenuStatus) {
        Menu menu = shopMenuService.updateMenuStatus(shopId, menuId, shopMenuStatus);
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

    @GetMapping("/v1/owner/shops/{shopId}/dashboard")
    public ResponseEntity<ApiResponse<ShopDashboardResponse>> getDashboardByShopId(@PathVariable Long shopId) {
        return ApiResponse.success(shopOwnerService.getShopDashboardByShopId(shopId));
    }
}
