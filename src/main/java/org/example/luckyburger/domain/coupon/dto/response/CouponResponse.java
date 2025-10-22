package org.example.luckyburger.domain.coupon.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.enums.CouponType;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record CouponResponse(
        Long id,

        String name,

        Double discount,

        int count,

        LocalDateTime expirationDate,

        CouponType type,

        LocalDateTime createAt
) {
    public static CouponResponse of(
            Long id,
            String name,
            Double discount,
            int count,
            LocalDateTime expirationDate,
            CouponType type,
            LocalDateTime createAt) {
        return CouponResponse.builder()
                .id(id)
                .name(name)
                .discount(discount)
                .count(count)
                .expirationDate(expirationDate)
                .type(type)
                .createAt(createAt)
                .build();
    }

    public static CouponResponse from(Coupon coupon) {
        return CouponResponse.builder()
                .id(coupon.getId())
                .name(coupon.getName())
                .discount(coupon.getDiscount())
                .count(coupon.getCount())
                .expirationDate(coupon.getExpirationDate())
                .type(coupon.getType())
                .createAt(coupon.getCreatedAt())
                .build();
    }
}
