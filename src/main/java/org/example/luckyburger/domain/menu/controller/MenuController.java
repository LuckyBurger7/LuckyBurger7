package org.example.luckyburger.domain.menu.controller;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.service.MenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Getter
@RestController
@RequestMapping("/api/v1/menus")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuController {

    private final MenuService menuService;

    // 메뉴 전체 조회
    @GetMapping
    public ResponseEntity<ApiPageResponse<MenuResponse>> getAllMenu(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<MenuResponse> response = menuService.getAllMenuResponse(pageable);

        return ApiPageResponse.success(response);
    }

    // 메뉴 단일 조회
    @GetMapping("/{menuId}")
    public ResponseEntity<ApiResponse<MenuResponse>> getMenu(
            @PathVariable Long menuId
    ) {
        MenuResponse response = menuService.getMenuResponse(menuId);

        return ApiResponse.success(response);
    }
}
