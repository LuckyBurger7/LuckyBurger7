package org.example.luckyburger.domain.shop.controller;

import lombok.AllArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.service.ShopAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Secured(AccountRole.Authority.ADMIN)
public class ShopAdminController {

    private final ShopAdminService shopAdminService;

    @PostMapping("/v1/admin/shops")
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(@RequestBody ShopRequest shopRequest) {

        return ApiResponse.created(shopAdminService.createShop(shopRequest));

    }

    @PutMapping("/v1/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShop(
            @PathVariable Long shopId,
            @RequestBody ShopRequest shopRequest) {

        return ApiResponse.success(shopAdminService.updateShop(shopId, shopRequest));
    }

    @DeleteMapping("/v1/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long shopId) {

        shopAdminService.deleteShop(shopId);

        return ApiResponse.noContent();
    }

}
