package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.shop.dto.request.CreateShopRequest;
import org.example.luckyburger.domain.shop.dto.request.UpdateShopRequest;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.service.ShopMenuService;
import org.example.luckyburger.domain.shop.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class ShopController {

    private final ShopService shopService;
    private final ShopMenuService shopMenuService;

    @PostMapping("/admin/shops")
    public ResponseEntity<ApiResponse<Shop>> shop(@RequestBody CreateShopRequest createShopRequest) {

        Shop shop = shopService.createShop(createShopRequest);

        return ApiResponse.created(shop);

    }

    @GetMapping("/shops/search")
    public ResponseEntity<ApiResponse<Shop>> findShopDetail(@RequestParam String shopName) {

        Shop shopDetail = shopService.findShopDetail(shopName);

        return ApiResponse.success(shopDetail);
    }

    @PutMapping("/owner/shops/{shopId}/{shopStatus}")
    public ResponseEntity<ApiResponse<Shop>> changeShopStatus(@PathVariable Long shopId,
                                 @PathVariable BusinessStatus shopStatus) {

        Shop shop = shopService.changeStatus(shopId, shopStatus);

        return ApiResponse.success(shop);
    }

    @PutMapping("/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<Shop>> ShopUpdate(@PathVariable Long shopId,
                                                        @RequestBody UpdateShopRequest updateShopRequest) {

        Shop shop = shopService.updateShop(shopId, updateShopRequest);

        return ApiResponse.success(shop);
    }

    @GetMapping("/admin/shops/count")
    public Long countShop(){

        Long l = shopService.countShop();

        return l;
    }

    @DeleteMapping("/admin/shops/{shopId}")
    public void deleteShop(@PathVariable Long shopId) {

        shopService.deleteShop(shopId);
    }

    // 메뉴 - ShopMenu 엔티티 활용

    //Page로 변경?
    @GetMapping("/shops/{shopId}/menus")
    public ResponseEntity<ApiResponse<List<Menu>>> getAllShopMenu(@PathVariable Long shopId){

        List<Menu> shopMenuList = shopMenuService.getShopMenuList(shopId);

        return ApiResponse.success(shopMenuList);
    }

    @GetMapping("/shops/{shopId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<Menu>> getMenuDetail(@PathVariable Long shopId,
                                                           @PathVariable Long menuId){

        Menu menuDetail = shopMenuService.getMenuDetail(shopId, menuId);

        return ApiResponse.success(menuDetail);
    }

    //shopId는 접속된 로그인에서 추출해서 사용하도록 변경
    @PutMapping("/shops/{shopId}/menus/{menuId}/availability")
    public ResponseEntity<ApiResponse<Menu>> updateMenuStatusByOwner(@PathVariable Long shopId,
                                                                     @PathVariable Long menuId,
                                                                     @RequestBody ShopMenuStatus shopMenuStatus){
        Menu menu = shopMenuService.updateMenuStatus(shopId, menuId,shopMenuStatus);

        return ApiResponse.success(menu);
    }

    // 매출
    @GetMapping("/admin/shops/sales/total")
    public int getTotalSaleByShop(Long shopId){

        int total = shopService.getTotal(shopId);

        return total;
    }

    @GetMapping("/owner/shops/{shopId}/orders/daily?date=YYYY-MM-DD")
    public List<Order> getOrderTodayByShop(@RequestParam LocalDateTime localDateTime,
                                     @PathVariable Long shopId){

        List<Order> orderTodayByShop = shopService.getOrderTodayByShop(localDateTime, shopId);

        return orderTodayByShop;

    }

    @GetMapping("/owner/shops/{shopId}/sales/daily?date=YYYY-MM-DD")
    public int getTotalOrderPrice(@PathVariable Long shopId, @RequestParam LocalDateTime localDateTime){

        int totalSaleToday = shopService.getTotalsaleToday(shopId, localDateTime);

        return totalSaleToday;


    }

    @GetMapping("/owner/shops/{shopId}/sales/monthly?month=YYYY-MM")
    public int getTotalSaleByMonthWithShopId(@PathVariable Long shopId, @RequestParam LocalDateTime localDateTime){

        int totalSaleByMonthWithShopId = shopService.getTotalSaleByMonthWithShopId(shopId, localDateTime);

        return totalSaleByMonthWithShopId;

    }

    @GetMapping("/owner/shops/{shopId}/ratings")
    public int getRatingByShop(@PathVariable Long shopId){

        int ratingByShop = shopService.getRatingByShop(shopId);

        return ratingByShop;
    }

    @PutMapping("/owner/shops/{shopId}/coupons/{couponId}/availability")
    public boolean couponAvailabilityByOwner(@PathVariable Long shopId,
                                             @PathVariable Long couponId){

        boolean couponListByShopId = shopService.getCouponListByShopId(shopId, couponId);

        return couponListByShopId;
    }



}
