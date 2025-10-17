package org.example.luckyburger.domain.menu.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
import org.example.luckyburger.domain.menu.dto.request.MenuUpdateRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.service.MenuAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Getter
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Secured(AccountRole.Authority.ADMIN)
public class MenuAdminController {

    private final MenuAdminService menuAdminService;

    @PostMapping("/v1/admin/menus")
    public ResponseEntity<ApiResponse<MenuResponse>> createMenu(
            @Valid @RequestBody MenuCreateRequest menuCreateRequest
    ) {
        return ApiResponse.created(menuAdminService.createMenu(menuCreateRequest));
    }

    @PutMapping("/v1/admin/menus/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest
    ) {
        return ApiResponse.success(menuAdminService.updateMenu(menuId, menuUpdateRequest));
    }

    @DeleteMapping("/v1/admin/menus/{menuId}")
    public void deleteMenu(@PathVariable Long menuId) {
        menuAdminService.deleteMenu(menuId);
    }

}
