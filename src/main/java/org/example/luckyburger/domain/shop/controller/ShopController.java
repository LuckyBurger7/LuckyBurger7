package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopMenuService;
import org.example.luckyburger.domain.shop.service.ShopService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;
    private final ShopMenuService shopMenuService;

    @GetMapping("/v1/shops/search")
    public ResponseEntity<ApiResponse<ShopResponse>> findShopDetail(@RequestParam String shopName) {

        ShopResponse shopDetail = shopService.getShopDetail(shopName);

        return ApiResponse.success(shopDetail);
    }

    // 메뉴 - ShopMenu 엔티티 활용

    //Page로 변경?
    @GetMapping("/v1/shops/{shopId}/menus")
    public ResponseEntity<ApiPageResponse<Menu>> getAllShopMenu(@PathVariable Long shopId,
                                                                  @RequestParam(defaultValue="0") int page,
                                                                  @RequestParam(defaultValue="10") int size){

        Page<Menu> shopMenuPage = shopMenuService.getShopMenuList(shopId, page, size);

        return ApiPageResponse.success(shopMenuPage);

    }

    @GetMapping("/v1/shops/{shopId}/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenuDetail(@PathVariable Long shopId,
                                                           @PathVariable Long menuId){

        MenuResponse menuDetail = shopMenuService.getMenuDetail(shopId, menuId);

        return ApiResponse.success(menuDetail);
    }

}
