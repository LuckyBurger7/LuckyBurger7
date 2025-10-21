package org.example.luckyburger.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.order.dto.request.OrderUpdateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.service.OrderOwnerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Secured(AccountRole.Authority.OWNER)
public class OrderOwnerController {
    private final OrderOwnerService orderOwnerService;

    @GetMapping("/v1/owner/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable("orderId") Long orderId) {
        return ApiResponse.success(orderOwnerService.getOrderResponse(orderId));
    }

    @GetMapping("/v1/owner/orders")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiPageResponse.success(orderOwnerService.getAllOrderResponse(pageable));
    }

    @PutMapping("/v1/owner/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderUpdateRequest request) {
        orderOwnerService.updateOrderStatus(orderId, request);
        return ApiResponse.noContent();
    }
}
