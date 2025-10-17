package org.example.luckyburger.domain.order.dto.request;

import jakarta.validation.constraints.*;

public record OrderCreateRequest(
        @NotNull
        Long shopId,
        @NotBlank @Size(max = 50)
        String receiver,
        @NotBlank @Pattern(regexp = "^01[0-9]-?[0-9]{3,4}-?[0-9]{4}$")
        String phone,
        @NotBlank @Size(max = 255)
        String address,
        @NotBlank @Size(max = 100)
        String street,
        @Size(max = 255)
        String request,
        Long couponId,
        @PositiveOrZero
        Integer point
) {
}


