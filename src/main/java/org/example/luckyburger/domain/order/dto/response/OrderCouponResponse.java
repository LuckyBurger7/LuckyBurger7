package org.example.luckyburger.domain.order.dto.response;

import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.coupon.enums.CouponType;

@Builder
public record OrderCouponResponse(
        Long couponId,
        String name,
        double discount,
        CouponType type
) {
    public static OrderCouponResponse of(Coupon coupon) {
        return OrderCouponResponse.builder()
                .couponId(coupon.getId())
                .name(coupon.getName())
                .discount(coupon.getDiscount())
                .type(coupon.getType())
                .build();
    }

    public static OrderCouponResponse from(UserCoupon userCoupon) {
        return OrderCouponResponse.of(userCoupon.getCoupon());
    }
}
