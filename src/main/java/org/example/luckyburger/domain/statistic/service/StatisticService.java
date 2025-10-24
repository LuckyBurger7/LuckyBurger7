package org.example.luckyburger.domain.statistic.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.coupon.service.CouponEntityFinder;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.service.MenuEntityFinder;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.example.luckyburger.domain.shop.service.ShopMenuEntityFinder;
import org.example.luckyburger.domain.statistic.dto.response.AdminDashboardResponse;
import org.example.luckyburger.domain.statistic.dto.response.MenuTotalSalesResponse;
import org.example.luckyburger.domain.statistic.dto.response.MonthTotalSalesResponse;
import org.example.luckyburger.domain.statistic.dto.response.ShopTotalSalesResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class StatisticService {

    private final OrderEntityFinder orderEntityFinder;
    private final ShopMenuEntityFinder shopMenuEntityFinder;
    private final ShopEntityFinder shopEntityFinder;
    private final MenuEntityFinder menuEntityFinder;
    private final CouponEntityFinder couponEntityFinder;

    public List<MonthTotalSalesResponse> getAllMonthTotalSalesResponse() {

        return orderEntityFinder.getAllMonthTotalSalesResponse();
    }

    public List<ShopTotalSalesResponse> getTopTenShopTotalSalesResponse() {

        return orderEntityFinder.getAllShopTotalSalesResponseOrderByDesc();
    }

    public List<ShopTotalSalesResponse> getBottomTenShopTotalSalesResponse() {

        return orderEntityFinder.getAllShopTotalSalesResponseOrderByAsc();
    }

    public List<MenuTotalSalesResponse> getMenuTotalSalesResponse(MenuCategory category) {

        List<Long> categoryMenuIds = menuEntityFinder.getAllMenuIdByCategory(category);

        return shopMenuEntityFinder.getAllMenuTotalSalesByMenuIds(categoryMenuIds);

    }

    public AdminDashboardResponse getAdminDashboardResponse() {
        return AdminDashboardResponse.of(
                shopEntityFinder.countShops(),
                orderEntityFinder.getCountOrderByCompleted(),
                couponEntityFinder.getCountAvailableCoupon(),
                shopMenuEntityFinder.getSumOfSalesVolumes()
        );
    }
}
