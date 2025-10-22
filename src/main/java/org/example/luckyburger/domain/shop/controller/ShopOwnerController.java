package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopDashboardResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopMenuService;
import org.example.luckyburger.domain.shop.service.ShopOwnerService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Secured(AccountRole.Authority.OWNER)
public class ShopOwnerController {

    private final ShopMenuService shopMenuService;
    private final ShopOwnerService shopOwnerService;

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
    @PutMapping("/v1/owner/shops/{shopId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<Menu>> updateMenuStatusByOwner(@PathVariable Long shopId,
                                                                     @PathVariable Long menuId,
                                                                     @RequestBody ShopMenuStatus shopMenuStatus) {
        Menu menu = shopMenuService.updateMenuStatus(shopId, menuId, shopMenuStatus);

        return ApiResponse.success(menu);
    }

    @GetMapping("/v1/owner/shops/{shopId}/dashboard")
    public ResponseEntity<ApiResponse<ShopDashboardResponse>> getDashboardByShopId(@PathVariable Long shopId) {
        return ApiResponse.success(shopOwnerService.getShopDashboardByShopId(shopId));
    }
}
