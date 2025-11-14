package org.example.luckyburger.domain.cart.controller;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.service.CartCacheUserService;
import org.example.luckyburger.domain.cart.service.CartUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@Secured(AccountRole.Authority.USER)
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
public class CartUserController {

    private final CartUserService cartUserService;
    private final CartCacheUserService cartCacheUserService;

    @PostMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<Void>> addMenu(
            @Valid @RequestBody CartAddMenuRequest request
    ) {
        cartUserService.addCartMenu(request);

        return ApiResponse.noContent();
    }

    @PostMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<Void>> addMenuApplyCache(
            @Valid @RequestBody CartAddMenuRequest request
    ) {
        cartCacheUserService.addCartMenu(request);

        return ApiResponse.noContent();
    }

    @GetMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ApiResponse.success(cartUserService.getCartResponse());
    }

    @GetMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> getCartApplyCache() {
        return ApiResponse.success(cartCacheUserService.getCartResponse());
    }

    @PutMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(
            @Valid @RequestBody CartUpdateMenuRequest request
    ) {
        return ApiResponse.success(cartUserService.updateCartMenu(request));
    }

    @PutMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartApplyCache(
            @Valid @RequestBody CartUpdateMenuRequest request
    ) {
        return ApiResponse.success(cartCacheUserService.updateCartMenu(request));
    }

    @DeleteMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> deleteCart(
            @Valid @RequestBody CartDeleteMenuRequest request
    ) {
        return ApiResponse.success(cartUserService.deleteCartMenu(request));
    }

    @DeleteMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> deleteCartApplyCache(
            @Valid @RequestBody CartDeleteMenuRequest request
    ) {
        return ApiResponse.success(cartCacheUserService.deleteCartMenu(request));
    }

}
