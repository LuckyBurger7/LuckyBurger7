package org.example.luckyburger.domain.coupon.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.enums.CouponType;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record CouponResponse(
        String name,

        Double discount,

        int count,

        LocalDateTime expirationDate,

        CouponType type,

        LocalDateTime createAt
) {
    public static CouponResponse of(Coupon coupon) {
        return CouponResponse.builder()
                .name(coupon.getName())
                .discount(coupon.getDiscount())
                .count(coupon.getCount())
                .expirationDate(coupon.getExpirationDate())
                .type(coupon.getType())
                .createAt(coupon.getCreatedAt())
                .build();
    }

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .name(coupon.getName())
                .discount(coupon.getDiscount())
                .count(coupon.getCount())
                .expirationDate(coupon.getExpirationDate())
                .type(coupon.getType())
                .createAt(coupon.getCreatedAt())
                .build();
    }
}
