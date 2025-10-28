package org.example.luckyburger.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.shop.enums.CouponStatus;

public record CouponPolicyRequest(
        @NotNull(message = "쿠폰 상태는 필수값입니다.")
        CouponStatus couponStatus
) {
}
