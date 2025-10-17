package org.example.luckyburger.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.dto.request.UserUpdateRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/signup")
    public ResponseEntity<ApiResponse<UserResponse>> signupUser(@Valid @RequestBody UserSignupRequest request) {
        return ApiResponse.created(userService.createUser(request));
    }

    @PutMapping("/v1/user/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UserUpdateRequest request) {
        return ApiResponse.created(userService.updateProfile(request));
    }

    @GetMapping("/v1/user/profile")
    public ResponseEntity<ApiResponse<UserResponse>> getProfile() {
        return ApiResponse.created(userService.getProfile());
    }
}
