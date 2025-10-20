package org.example.luckyburger.domain.order.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.request.OrderPrepareRequest;
import org.example.luckyburger.domain.order.dto.response.OrderPrepareResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.service.OrderUserService;
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

    @GetMapping("/v1/user/orders/info")
    public ResponseEntity<ApiResponse<OrderPrepareResponse>> prepareOrder(@AuthenticationPrincipal AuthAccount authAccount,
                                                                          @Valid @RequestBody OrderPrepareRequest request) {
        return ApiResponse.success(orderUserService.prepareOrder(authAccount, request));
    }

    @PostMapping("/v1/user/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@AuthenticationPrincipal AuthAccount authAccount,
                                                                  @Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.created(orderUserService.createOrder(authAccount, request));
    }

    @GetMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@AuthenticationPrincipal AuthAccount authAccount,
                                                               @PathVariable("orderId") Long orderId) {
        return ApiResponse.success(orderUserService.getOrder(authAccount, orderId));
    }

    @GetMapping("/v1/user/orders")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getAllOrder(
            @AuthenticationPrincipal AuthAccount authAccount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiPageResponse.success(orderUserService.getAllOrder(authAccount, pageable));
    }

    @PutMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> deleteOrder(
            @AuthenticationPrincipal AuthAccount authAccount,
            @PathVariable("orderId") Long orderId) {
        orderUserService.deleteOrder(authAccount, orderId);
        return ApiResponse.noContent();
    }
}
