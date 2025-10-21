package org.example.luckyburger.domain.cart.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.service.CartUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
public class CartUserController {

    private final CartUserService cartUserService;

    @PostMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<Void>> addMenu(
            @Valid @RequestBody CartAddMenuRequest request
    ) {
        cartUserService.addCartMenu(request);

        return ApiResponse.noContent();
    }

    @GetMapping("v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ApiResponse.success(cartUserService.getCartResponse());
    }

    @PutMapping("v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(
            @Valid @RequestBody CartUpdateMenuRequest request
    ) {
        return ApiResponse.success(cartUserService.updateCartMenu(request));
    }

    @DeleteMapping("v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> removeCart(
            @Valid @RequestBody CartDeleteMenuRequest request
    ) {
        return ApiResponse.success(cartUserService.deleteCartMenu(request));
    }
}
