package org.example.luckyburger.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserUpdateRequest(
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
