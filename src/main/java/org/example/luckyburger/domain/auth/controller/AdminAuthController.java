package org.example.luckyburger.domain.auth.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.dto.request.OwnerSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.OwnerUpdateRequest;
import org.example.luckyburger.domain.auth.dto.response.OwnerResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AdminAuthService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
@Secured(AccountRole.Authority.ADMIN)
public class AdminAuthController {

    private final AdminAuthService adminAuthService;

    @PostMapping("/v1/admin/ownerSignup")
    public ResponseEntity<ApiResponse<OwnerResponse>> createOwner(@RequestBody OwnerSignupRequest ownerSignupRequest) {
        return ApiResponse.created(adminAuthService.createOwner(ownerSignupRequest));
    }

    @PutMapping("/v1/admin/owners/{ownerId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> updateOwner(
            @PathVariable Long ownerId,
            @RequestBody OwnerUpdateRequest ownerUpdateRequest) {
        return ApiResponse.created(adminAuthService.updateOwner(ownerId, ownerUpdateRequest));
    }

    @DeleteMapping("/v1/admin/owners/{ownerId}")
    public ResponseEntity<ApiResponse<Void>> deleteOwner(@PathVariable Long ownerId) {
        adminAuthService.deleteOwner(ownerId);

        return ApiResponse.noContent();
    }

    @GetMapping("/v1/admin/owners")
    public ResponseEntity<ApiPageResponse<OwnerResponse>> getAllOwnerResponse(@PageableDefault Pageable pageable) {

        return ApiPageResponse.success(adminAuthService.getAllOwnerResponse(pageable));
    }

}
