package org.example.luckyburger.domain.coupon.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.coupon.enums.CouponType;

import java.time.LocalDateTime;

public record CouponRequest(
        @NotBlank(message = "쿠폰 이름은 필수 입력값입니다.")
        String name,

        @NotNull(message = "할인 금액은 필수 입력값입니다.")
        Double discount,

        @NotNull(message = "쿠폰 수량은 필수 입력값입니다.")
        int count,

        @NotNull(message = "만료일은 필수 입력값입니다.")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expirationDate,

        @NotNull(message = "쿠폰 타입은 필수 입력값입니다.")
        CouponType type
) {
}
