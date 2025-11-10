package org.example.luckyburger.domain.order.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.order.dto.request.OrderUpdateRequest;
import org.example.luckyburger.domain.order.dto.response.OrderResponse;
import org.example.luckyburger.domain.order.service.OrderOwnerService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
@Secured(AccountRole.Authority.OWNER)
public class OrderOwnerController {
    private final OrderOwnerService orderOwnerService;

    @GetMapping("/v1/owner/orders/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable("orderId") Long orderId) {
        return ApiResponse.success(orderOwnerService.getOrderResponse(orderId));
    }

    @GetMapping("/v2/owner/orders")
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
        return ApiPageResponse.success(orderOwnerService.getAllOrderResponse(pageable));
    }

    @GetMapping("/v1/owner/orders")
    public ResponseEntity<ApiPageResponse<OrderResponse>> getAllOrderCompareNoIndex(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return ApiPageResponse.success(orderOwnerService.getAllOrderResponseCompare(pageable));
    }

    @PutMapping("/v1/owner/orders/{orderId}")
    public ResponseEntity<ApiResponse<Void>> updateOrderStatus(
            @PathVariable("orderId") Long orderId,
            @Valid @RequestBody OrderUpdateRequest request) {
        orderOwnerService.updateOrderStatus(orderId, request);
        return ApiResponse.noContent();
    }
}
