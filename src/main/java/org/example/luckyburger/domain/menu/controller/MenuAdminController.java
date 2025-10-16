package org.example.luckyburger.domain.menu.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
import org.example.luckyburger.domain.menu.dto.request.MenuUpdateRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuCreateResponse;
import org.example.luckyburger.domain.menu.dto.response.MenuUpdateResponse;
import org.example.luckyburger.domain.menu.service.MenuAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Getter
@RestController
@RequestMapping("/api/v1/admin/menus")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Secured(AccountRole.Authority.ADMIN)
public class MenuAdminController {

    private final MenuAdminService menuAdminService;

    @PostMapping
    public ResponseEntity<ApiResponse<MenuCreateResponse>> createMenu(
            @Valid @RequestBody MenuCreateRequest menuCreateRequest
    ) {
        return ApiResponse.created(menuAdminService.createMenu(menuCreateRequest));
    }

    @PutMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuUpdateResponse>> updateMenu(
            @PathVariable Long menuId,
            @Valid @RequestBody MenuUpdateRequest menuUpdateRequest
    ) {
        return ApiResponse.success(menuAdminService.updateMenu(menuId, menuUpdateRequest));
    }

    @DeleteMapping("/{menuId}")
    public void deleteMenu(@PathVariable Long menuId) {
        menuAdminService.deleteMenu(menuId);
    }

}
