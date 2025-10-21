package org.example.luckyburger.domain.order.dto.response;

import lombok.Builder;

@Builder
public record OrderCouponResponse(
        Long couponId,
        String name,
        long price
) {
    public static OrderCouponResponse of(Long couponId, String couponName, long price) {
        return OrderCouponResponse.builder()
                .couponId(couponId)
                .name(couponName)
                .price(price)
                .build();
    }
}
