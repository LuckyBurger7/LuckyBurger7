package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopMenuService;
import org.example.luckyburger.domain.shop.service.ShopService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class ShopAdminController {

    private final ShopService shopService;
    private final ShopMenuService shopMenuService;

    @PostMapping("/v1/admin/shops")
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(@RequestBody ShopRequest shopRequest) {

        ShopResponse shop = shopService.createShop(shopRequest);

        return ApiResponse.created(shop);

    }

    @PutMapping("/v1/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShop(@PathVariable Long shopId,
                                                        @RequestBody ShopRequest shopRequest) {

        ShopResponse shopresponse = shopService.updateShop(shopId, shopRequest);

        return ApiResponse.success(shopresponse);
    }

    @DeleteMapping("/v1/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long shopId) {

        shopService.deleteShop(shopId);

        return ApiResponse.noContent();
    }

//    @GetMapping("/v1/admin/shops/sales/total")
//    public ResponseEntity<ApiResponse<Integer>> getTotalSaleByShop(Long shopId){
//
//        Integer total = shopService.getTotal(shopId);
//
//        return ApiResponse.success(total);
//    }

}
