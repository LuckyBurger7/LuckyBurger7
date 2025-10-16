package org.example.luckyburger.domain.auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.auth.dto.response.AuthResponse;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/v1/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> userSignup(@Valid @RequestBody UserSignupRequest request) {
        return ApiResponse.created(authService.userSignup(request));
    }

    @GetMapping("/v1/test")
    public String test() {
        return authService.loginTest();
    }
}
