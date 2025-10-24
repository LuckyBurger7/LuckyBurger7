package org.example.luckyburger.domain.order.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderPrepareResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.service.OrderUserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
public class OrderUserController {
    private final OrderUserService orderUserService;

    @GetMapping("/v1/user/orderInfo")
    public ResponseEntity<ApiResponse<OrderPrepareResponse>> prepareOrder() {
        return ApiResponse.success(orderUserService.prepareOrderResponse());
    }

    @PostMapping("/v1/user/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.created(orderUserService.createOrderResponse(request));
    }

    @GetMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable("orderId") Long orderId) {
        return ApiResponse.success(orderUserService.getOrderResponse(orderId));
    }

    @GetMapping("/v1/user/orders")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ApiPageResponse.success(orderUserService.getAllOrderResponse(pageable));
    }

    @PutMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable("orderId") Long orderId) {
        orderUserService.cancelOrder(orderId);
        return ApiResponse.noContent();
    }
}
