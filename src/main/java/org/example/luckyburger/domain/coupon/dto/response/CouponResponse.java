package org.example.luckyburger.domain.coupon.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.enums.CouponType;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record CouponResponse(
        Long couponId,

        String name,

        Double discount,

        int count,

        LocalDateTime expirationDate,

        CouponType couponType,

        LocalDateTime createAt
) {
    public static CouponResponse of(
            Long couponId,
            String name,
            Double discount,
            int count,
            LocalDateTime expirationDate,
            CouponType couponType,
            LocalDateTime createAt) {
        return CouponResponse.builder()
                .couponId(couponId)
                .name(name)
                .discount(discount)
                .count(count)
                .expirationDate(expirationDate)
                .couponType(couponType)
                .createAt(createAt)
                .build();
    }

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .discount(coupon.getDiscount())
                .count(coupon.getCount())
                .expirationDate(coupon.getExpirationDate())
                .couponType(coupon.getType())
                .createAt(coupon.getCreatedAt())
                .build();
    }
}
