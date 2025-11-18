package org.example.luckyburger.domain.cart.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.cart.dto.request.CartAddMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartDeleteMenuRequest;
import org.example.luckyburger.domain.cart.dto.request.CartUpdateMenuRequest;
import org.example.luckyburger.domain.cart.dto.response.CartResponse;
import org.example.luckyburger.domain.cart.service.CartCacheUserService;
import org.example.luckyburger.domain.cart.service.CartUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
public class CartUserController {

    private final CartUserService cartUserService;
    private final CartCacheUserService cartCacheUserService;
    /*
    @PostMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<Void>> addMenu(
            @Valid @RequestBody CartAddMenuRequest request
    ) {
        cartUserService.addCartMenu(request);

        return ApiResponse.noContent();
    }
     */

    @Operation(summary = "장바구니 담기")
    @PostMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<Void>> addMenuApplyCache(
            @Valid @RequestBody CartAddMenuRequest request
    ) {
        cartCacheUserService.addCartMenu(request);

        return ApiResponse.noContent();
    }

    /*
    @GetMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> getCart() {
        return ApiResponse.success(cartUserService.getCartResponse());
    }
     */

    @Operation(summary = "장바구니 메뉴 확인")
    @GetMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> getCartApplyCache() {
        return ApiResponse.success(cartCacheUserService.getCartResponse());
    }

    /*
    @PutMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> updateCart(
            @Valid @RequestBody CartUpdateMenuRequest request
    ) {
        return ApiResponse.success(cartUserService.updateCartMenu(request));
    }
    */

    @Operation(summary = "장바구니 메뉴 수정")
    @PutMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> updateCartApplyCache(
            @Valid @RequestBody CartUpdateMenuRequest request
    ) {
        return ApiResponse.success(cartCacheUserService.updateCartMenu(request));
    }

    /*
    @DeleteMapping("/v1/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> deleteCart(
            @Valid @RequestBody CartDeleteMenuRequest request
    ) {
        return ApiResponse.success(cartUserService.deleteCartMenu(request));
    }
     */

    @Operation(summary = "장바구니 삭제")
    @DeleteMapping("/v2/user/carts")
    public ResponseEntity<ApiResponse<CartResponse>> deleteCartApplyCache(
            @Valid @RequestBody CartDeleteMenuRequest request
    ) {
        return ApiResponse.success(cartCacheUserService.deleteCartMenu(request));
    }

}
