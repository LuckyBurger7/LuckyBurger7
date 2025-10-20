package org.example.luckyburger.domain.coupon.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.coupon.enums.CouponType;

import java.time.LocalDateTime;

public record CouponRequest(
        @NotBlank
        String name,

        @NotNull
        Double discount,

        @NotNull
        int count,

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expirationDate,

        @NotNull
        CouponType type
) {
}
