package org.example.luckyburger.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.dto.request.CredentialRequest;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.dto.request.UserUpdateRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원 가입")
    @PostMapping("/v1/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signupUser(@Valid @RequestBody UserSignupRequest request) {
        return ApiResponse.created(userService.createUser(request));
    }

    @Operation(summary = "회원 정보 수정")
    @Secured(AccountRole.Authority.USER)
    @PutMapping("/v1/user/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.created(userService.updateProfile(request));
    }

    @Operation(summary = "회원 정보 확인")
    @Secured(AccountRole.Authority.USER)
    @GetMapping("/v1/user/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        return ApiResponse.created(userService.getProfile());
    }

    @Operation(summary = "회원 탈퇴")
    @Secured(AccountRole.Authority.USER)
    @DeleteMapping("/v1/withdraw")
    public ResponseEntity<ApiResponse<Void>> withdraw(@Valid @RequestBody CredentialRequest request) {
        userService.withdrawUser(request);
        return ApiResponse.noContent();
    }
}
