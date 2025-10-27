package org.example.luckyburger.domain.shop.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.example.luckyburger.domain.shop.enums.CouponStatus;

import java.time.LocalDateTime;

@Builder
public record CouponPolicyResponse(
        Long couponPolicyId,
        Long couponId,
        String name,
        LocalDateTime expirationDate,
        CouponStatus couponStatus
) {
    public static CouponPolicyResponse from(CouponPolicy couponPolicy) {
        return CouponPolicyResponse.builder()
                .couponPolicyId(couponPolicy.getId())
                .couponId(couponPolicy.getCoupon().getId())
                .name(couponPolicy.getCoupon().getName())
                .expirationDate(couponPolicy.getCoupon().getExpirationDate())
                .couponStatus(couponPolicy.getStatus())
                .build();
    }
}
