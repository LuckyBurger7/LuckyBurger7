package org.example.luckyburger.domain.order.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.order.dto.request.OrderCreateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderPrepareResponse;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.service.OrderUserService;
import org.example.luckyburger.domain.order.service.OrderUserServiceV2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
public class OrderUserController {
    private final OrderUserService orderUserService;
    private final OrderUserServiceV2 orderUserServiceV2;

    /*
    @GetMapping("/v1/user/orderInfo")
    public ResponseEntity<ApiResponse<OrderPrepareResponse>> prepareOrder() {
        return ApiResponse.success(orderUserService.prepareOrderResponse());
    }
     */
    /*
    @PostMapping("/v1/user/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.created(orderUserService.createOrderResponse(request));
    }
     */

    @Operation(summary = "주문 단건 확인")
    @GetMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable("orderId") Long orderId) {
        return ApiResponse.success(orderUserService.getOrderResponse(orderId));
    }

    @Operation(summary = "주문 전체 확인")
    @GetMapping("/v1/user/orders")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getAllOrder(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(
                page, size,
                Sort.by(
                        Sort.Order.desc("orderDate"),
                        Sort.Order.desc("id")
                )
        );
        return ApiPageResponse.success(orderUserService.getAllOrderResponse(pageable));
    }

    @Operation(summary = "주문 취소")
    @PutMapping("/v1/user/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(
            @PathVariable("orderId") Long orderId) {
        orderUserService.cancelOrder(orderId);
        return ApiResponse.noContent();
    }

    @Operation(summary = "주문지 생성")
    @GetMapping("/v2/user/orderInfo")
    public ResponseEntity<ApiResponse<OrderPrepareResponse>> prepareOrderV2() {
        return ApiResponse.success(orderUserServiceV2.prepareOrderResponse());
    }

    @Operation(summary = "주문하기")
    @PostMapping("/v2/user/orders")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrderV2(@Valid @RequestBody OrderCreateRequest request) {
        return ApiResponse.created(orderUserServiceV2.createOrderResponse(request));
    }
}
