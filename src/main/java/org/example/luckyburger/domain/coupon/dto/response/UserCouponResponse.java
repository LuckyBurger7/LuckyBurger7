package org.example.luckyburger.domain.coupon.dto.response;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;

@Builder(access = AccessLevel.PRIVATE)
public record UserCouponResponse(
        Long userCouponId,

        VerifiedUserCouponResponse verifiedUserCouponResponse,

        LocalDateTime issueDate,

        LocalDateTime usedDate
) {
    public static UserCouponResponse from(UserCoupon userCoupon) {
        return UserCouponResponse.builder()
                .userCouponId(userCoupon.getId())
                .verifiedUserCouponResponse(VerifiedUserCouponResponse.from(userCoupon.getCoupon()))
                .issueDate(LocalDateTime.now())
                .usedDate(userCoupon.getUsedDate())
                .build();
    }
}
