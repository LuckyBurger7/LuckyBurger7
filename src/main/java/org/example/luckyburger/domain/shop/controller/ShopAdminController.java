package org.example.luckyburger.domain.shop.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.service.ShopAdminService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Secured(AccountRole.Authority.ADMIN)
public class ShopAdminController {

    private final ShopAdminService shopAdminService;

    @Operation(summary = "관리자 점포 생성")
    @PostMapping("/v1/admin/shops")
    public ResponseEntity<ApiResponse<ShopResponse>> createShop(@RequestBody ShopRequest shopRequest) {

        return ApiResponse.created(shopAdminService.createShop(shopRequest));

    }

    @Operation(summary = "관리자 전포 전체 조회")
    @GetMapping("/v1/admin/shops")
    public ResponseEntity<ApiPageResponse<ShopResponse>> getAllShop(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return ApiPageResponse.success(shopAdminService.getAllShopResponse(pageable));
    }

    @Operation(summary = "관리자 점포 수정")
    @PutMapping("/v1/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<ShopResponse>> updateShop(
            @PathVariable Long shopId,
            @RequestBody ShopRequest shopRequest) {

        return ApiResponse.success(shopAdminService.updateShop(shopId, shopRequest));
    }

    @Operation(summary = "관리자 점포 삭제")
    @DeleteMapping("/v1/admin/shops/{shopId}")
    public ResponseEntity<ApiResponse<Void>> deleteShop(@PathVariable Long shopId) {

        shopAdminService.deleteShop(shopId);

        return ApiResponse.noContent();
    }

}
