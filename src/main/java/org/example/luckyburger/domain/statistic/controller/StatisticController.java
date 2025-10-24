package org.example.luckyburger.domain.statistic.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.statistic.dto.response.AdminDashboardResponse;
import org.example.luckyburger.domain.statistic.dto.response.MenuTotalSalesResponse;
import org.example.luckyburger.domain.statistic.dto.response.MonthTotalSalesResponse;
import org.example.luckyburger.domain.statistic.dto.response.ShopTotalSalesResponse;
import org.example.luckyburger.domain.statistic.service.StatisticService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Secured(AccountRole.Authority.ADMIN)
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/v1/admin/statistics/sales/monthly")
    public ResponseEntity<ApiResponse<List<MonthTotalSalesResponse>>> getAllMonthTotalSalesResponse() {
        return ApiResponse.success(statisticService.getAllMonthTotalSalesResponse());
    }

    @GetMapping("/v1/admin/statistics/sales/shops/top10")
    public ResponseEntity<ApiResponse<List<ShopTotalSalesResponse>>> getTopTenShopTotalSalesResponse() {
        return ApiResponse.success(statisticService.getTopTenShopTotalSalesResponse());
    }

    @GetMapping("/v1/admin/statistics/sales/shops/bottom10")
    public ResponseEntity<ApiResponse<List<ShopTotalSalesResponse>>> getBottomTenShopTotalSalesResponse() {
        return ApiResponse.success(statisticService.getBottomTenShopTotalSalesResponse());
    }

    @GetMapping("/v1/admin/statistics/sales/menus/burger")
    public ResponseEntity<ApiResponse<List<MenuTotalSalesResponse>>> getBurgerMenuTotalSalesResponse() {
        return ApiResponse.success(statisticService.getMenuTotalSalesResponse(MenuCategory.HAMBURGER));
    }

    @GetMapping("/v1/admin/statistics/sales/menus/side")
    public ResponseEntity<ApiResponse<List<MenuTotalSalesResponse>>> getSideMenuTotalSalesResponse() {
        return ApiResponse.success(statisticService.getMenuTotalSalesResponse(MenuCategory.SIDE));
    }

    @GetMapping("/v1/admin/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getAdminDashboardResponse() {
        return ApiResponse.success(statisticService.getAdminDashboardResponse());
    }
}
