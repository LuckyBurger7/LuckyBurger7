package org.example.luckyburger.domain.coupon.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;

import java.time.LocalDateTime;

@Builder(access = AccessLevel.PRIVATE)
public record UserCouponResponse(
        Long id,

        CouponResponse couponResponse,

        LocalDateTime issueDate
) {
    public static UserCouponResponse from(UserCoupon userCoupon) {
        return UserCouponResponse.builder()
                .id(userCoupon.getId())
                .couponResponse(CouponResponse.from(userCoupon.getCoupon()))
                .issueDate(LocalDateTime.now())
                .build();
    }
}
