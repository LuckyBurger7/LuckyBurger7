package org.example.luckyburger.domain.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.user.dto.request.SignupUserRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/v1/signup")
    public ResponseEntity<ApiResponse<UserResponse>> userSignup(@Valid @RequestBody SignupUserRequest request) {
        return ApiResponse.created(userService.createUser(request));
    }

}
