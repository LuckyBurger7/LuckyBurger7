package org.example.luckyburger.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.dto.request.LoginRequest;
import org.example.luckyburger.domain.auth.dto.request.WithdrawRequest;
import org.example.luckyburger.domain.auth.dto.response.TokenResponse;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.success(authService.login(request));
    }

    @DeleteMapping("/v1/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@Valid @RequestBody WithdrawRequest request) {
        authService.withdraw(request);
        return ApiResponse.noContent();
    }
}
