package org.example.luckyburger.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.dto.response.ApiPageResponse;
import org.example.luckyburger.common.dto.response.ApiResponse;
import org.example.luckyburger.domain.auth.dto.request.OwnerSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.OwnerUpdateRequest;
import org.example.luckyburger.domain.auth.dto.response.OwnerResponse;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AuthAdminService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api")
@Secured(AccountRole.Authority.ADMIN)
public class AdminAuthController {

    private final AuthAdminService authAdminService;

    @Operation(summary = "점주 생성")
    @PostMapping("/v1/admin/ownerSignup")
    public ResponseEntity<ApiResponse<OwnerResponse>> createOwner(@RequestBody OwnerSignupRequest ownerSignupRequest) {
        return ApiResponse.created(authAdminService.createOwner(ownerSignupRequest));
    }

    @Operation(summary = "점주 수정")
    @PutMapping("/v1/admin/owners/{ownerId}")
    public ResponseEntity<ApiResponse<OwnerResponse>> updateOwner(
            @PathVariable Long ownerId,
            @RequestBody OwnerUpdateRequest ownerUpdateRequest) {
        return ApiResponse.created(authAdminService.updateOwner(ownerId, ownerUpdateRequest));
    }

    @Operation(summary = "점주 삭제")
    @DeleteMapping("/v1/admin/owners/{ownerId}")
    public ResponseEntity<ApiResponse<Void>> deleteOwner(@PathVariable Long ownerId) {
        authAdminService.deleteOwner(ownerId);

        return ApiResponse.noContent();
    }

    @Operation(summary = "점주 전체 조회")
    @GetMapping("/v1/admin/owners")
    public ResponseEntity<ApiPageResponse<OwnerResponse>> getAllOwnerResponse(@PageableDefault Pageable pageable) {

        return ApiPageResponse.success(authAdminService.getAllOwnerResponse(pageable));
    }

}
