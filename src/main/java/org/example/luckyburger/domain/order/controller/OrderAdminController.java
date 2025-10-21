package org.example.luckyburger.domain.order.controller;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.order.dto.response.OrderCountResponse;
import org.example.luckyburger.domain.order.service.OrderAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Secured(AccountRole.Authority.ADMIN)
public class OrderAdminController {
    private final OrderAdminService orderAdminService;

    @GetMapping("/v1/admin/orders/count")
    public ResponseEntity<ApiResponse<OrderCountResponse>> getOrderCount() {
        return ApiResponse.success(orderAdminService.getOrderCountResponse());
    }
}
