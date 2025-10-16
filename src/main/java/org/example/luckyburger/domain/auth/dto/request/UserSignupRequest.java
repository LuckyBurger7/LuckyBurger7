package org.example.luckyburger.domain.auth.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UserSignupRequest(
        @NotBlank
        String email,

        @NotBlank
        String password,
        
        @NotBlank
        String name,

        @NotBlank
        String phone,

        @NotBlank
        String address,

        @NotBlank
        String street
) {
}
