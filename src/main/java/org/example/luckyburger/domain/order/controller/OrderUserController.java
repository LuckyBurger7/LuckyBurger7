package org.example.luckyburger.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderCreateResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.service.OrderUserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderUserController {
    private final OrderUserService orderUserService;

    @PostMapping("/v1/user/orders")
    public ResponseEntity<ApiResponse<OrderCreateResponse>> createOrder(@AuthenticationPrincipal AuthAccount authAccount,
                                                                        @Valid @RequestBody OrderCreateRequest request) {
        OrderCreateResponse response = orderUserService.createOrder(authAccount, request);
        return ApiResponse.created(response);
    }

    @GetMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@AuthenticationPrincipal AuthAccount authAccount,
                                                               @PathVariable("orderId") Long orderId) {
        OrderResponse response = orderUserService.getOrder(authAccount, orderId);
        return ApiResponse.success(response);
    }

    @GetMapping("/v1/user/orders")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getAllOrder(
            @AuthenticationPrincipal AuthAccount authAccount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<OrderResponse> response = orderUserService.getAllOrder(authAccount, pageable);
        return ApiPageResponse.success(response);
    }

    @PutMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @AuthenticationPrincipal AuthAccount authAccount,
            @PathVariable("orderId") Long orderId) {
        orderUserService.deleteOrder(authAccount, orderId);
        return ApiResponse.success(null);
    }
}
