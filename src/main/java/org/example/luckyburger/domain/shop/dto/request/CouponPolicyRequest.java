package org.example.luckyburger.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;
import org.example.luckyburger.domain.shop.enums.CouponStatus;

public record CouponPolicyRequest(
        @NotNull
        CouponStatus couponStatus
) {
}
