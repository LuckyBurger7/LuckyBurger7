package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuResponse;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.service.ShopService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ShopController {

    private final ShopService shopService;

    @GetMapping("/v1/shops/search")
    public ResponseEntity<ApiPageResponse<ShopResponse>> searchShopDetail(
            @RequestParam(required = false) String shopName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ApiPageResponse.success(shopService.searchShopByName(shopName, pageable));
    }

    @GetMapping("/v1/shops/{shopId}/shopMenus")
    public ResponseEntity<ApiPageResponse<ShopMenuResponse>> getAllShopMenu(
            @PathVariable Long shopId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pageable = PageRequest.of(page, size);

        return ApiPageResponse.success(shopService.getAllShopMenuByShopIdResponse(shopId, pageable));
    }

    @GetMapping("/v1/shops/{shopId}/shopMenus/{shopMenuId}")
    public ResponseEntity<ApiResponse<ShopMenuResponse>> getMenuDetail(
            @PathVariable Long shopId,
            @PathVariable Long shopMenuId
    ) {
        return ApiResponse.success(shopService.getMenuDetail(shopId, shopMenuId));
    }

}
