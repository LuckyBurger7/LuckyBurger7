package org.example.luckyburger.domain.coupon.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.enums.CouponType;

@Builder(access = AccessLevel.PRIVATE)
public record VerifiedUserCouponResponse(
        Long couponId,

        String name,

        Double discount,

        LocalDateTime expirationDate,

        CouponType couponType,

        LocalDateTime createAt
) {
    public static VerifiedUserCouponResponse of(
            Long couponId,
            String name,
            Double discount,
            LocalDateTime expirationDate,
            CouponType couponType,
            LocalDateTime createAt) {
        return VerifiedUserCouponResponse.builder()
                .couponId(couponId)
                .name(name)
                .discount(discount)
                .expirationDate(expirationDate)
                .couponType(couponType)
                .createAt(createAt)
                .build();
    }

    public static VerifiedUserCouponResponse from(Coupon coupon) {
        return VerifiedUserCouponResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .discount(coupon.getDiscount())
                .expirationDate(coupon.getExpirationDate())
                .couponType(coupon.getType())
                .createAt(coupon.getCreatedAt())
                .build();
    }
}
