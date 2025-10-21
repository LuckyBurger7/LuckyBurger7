package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopMenuService;
import org.example.luckyburger.domain.shop.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ShopOwnerController {

    private final ShopService shopService;
    private final ShopMenuService shopMenuService;

    @PutMapping("/v1/owner/shops/{shopId}/{shopStatus}")
    public ResponseEntity<ApiResponse<Shop>> changeShopStatus(@PathVariable Long shopId,
                                                              @PathVariable BusinessStatus shopStatus) {

        Shop shop = shopService.updateStatus(shopId, shopStatus);

        return ApiResponse.success(shop);
    }

//    @GetMapping("/v1/owner/shops/{shopId}/sales/monthly?month=YYYY-MM")
//    public ResponseEntity<ApiResponse<Integer>> getTotalSaleByMonthWithShopId(@PathVariable Long shopId, @RequestParam LocalDateTime localDateTime){
//
//        int totalSaleByMonthWithShopId = shopService.getTotalSaleByMonthWithShopId(shopId, localDateTime);
//
//        return ApiResponse.success(totalSaleByMonthWithShopId);
//
//    }

    @GetMapping("/v1/owner/shops/{shopId}/ratings")
    public ResponseEntity<ApiResponse<Double>> getRatingByShop(@PathVariable Long shopId){

        double ratingByShop = shopService.getRatingByShop(shopId);

        return ApiResponse.success(ratingByShop);
    }

//    @PutMapping("/owner/shops/{shopId}/coupons/{couponId}/availability")
//    public boolean couponAvailabilityByOwner(@PathVariable Long shopId,
//                                             @PathVariable Long couponId){
//
//        boolean couponListByShopId = shopService.getCouponListByShopId(shopId, couponId);
//
//        return couponListByShopId;
//    }

    @GetMapping("/v1/owner/shops/{shopId}/orders/daily")
    public List<Order> getOrderTodayByShop(@RequestParam LocalDate date,
                                           @PathVariable Long shopId){
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Order> orderTodayByShop = shopService.getOrderTodayByShop(start, end, shopId);

        return orderTodayByShop;

    }
//
//    @GetMapping("/v1/owner/shops/{shopId}/sales/daily?date=YYYY-MM-DD")
//    public ResponseEntity<ApiResponse<Integer>> getTotalOrderPrice(@PathVariable Long shopId, @RequestParam LocalDateTime localDateTime){
//
//        int totalSaleToday = shopService.getTotalsaleToday(shopId, localDateTime);
//
//        return ApiResponse.success(totalSaleToday);
//
//    }

    //shopId는 접속된 로그인에서 추출해서 사용하도록 변경
    @PutMapping("/v1/shops/{shopId}/menus/{menuId}/{shopMenuStatus}")
    public ResponseEntity<ApiResponse<Menu>> updateMenuStatusByOwner(@PathVariable Long shopId,
                                                                     @PathVariable Long menuId,
                                                                     @PathVariable ShopMenuStatus shopMenuStatus){
        Menu menu = shopMenuService.updateMenuStatus(shopId, menuId,shopMenuStatus);

        return ApiResponse.success(menu);
    }
}
