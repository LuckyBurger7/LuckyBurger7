package org.example.luckyburger.domain.menu.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.menu.dto.request.MenuRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.service.MenuAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Secured(AccountRole.Authority.ADMIN)
public class MenuAdminController {

    private final MenuAdminService menuAdminService;

    @Operation(summary = "메뉴 추가")
    @PostMapping("/v1/admin/menus")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody MenuRequest menuRequest
    ) {
        return ApiResponse.created(menuAdminService.createMenu(menuRequest));
    }

    @Operation(summary = "메뉴 수정")
    @PutMapping("/v1/admin/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuRequest menuRequest
    ) {
        return ApiResponse.success(menuAdminService.updateMenu(menuId, menuRequest));
    }

    @Operation(summary = "메뉴 삭제")
    @DeleteMapping("/v1/admin/menus/{menuId}")
    public ResponseEntity<ApiResponse<Void>> deleteMenu(@PathVariable Long menuId) {
        menuAdminService.deleteMenu(menuId);

        return ApiResponse.noContent();
    }

}
