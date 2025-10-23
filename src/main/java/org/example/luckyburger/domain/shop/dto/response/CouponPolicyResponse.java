package org.example.luckyburger.domain.shop.dto.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.example.luckyburger.domain.shop.enums.CouponStatus;

@Builder
public record CouponPolicyResponse(
        Long id,
        Long couponId,
        String name,
        LocalDateTime expirationDate,
        CouponStatus status
) {
    public static CouponPolicyResponse from(CouponPolicy couponPolicy) {
        return CouponPolicyResponse.builder()
                .id(couponPolicy.getId())
                .couponId(couponPolicy.getCoupon().getId())
                .name(couponPolicy.getCoupon().getName())
                .expirationDate(couponPolicy.getCoupon().getExpirationDate())
                .status(couponPolicy.getStatus())
                .build();
    }
}
