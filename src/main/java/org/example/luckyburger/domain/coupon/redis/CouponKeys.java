package org.example.luckyburger.domain.coupon.redis;

public final class CouponKeys {
    private CouponKeys() {
    }

    public static String stockKey(Long couponId) {
        return "stock:coupon:" + couponId;
    }

    public static String issuedSetKey(Long couponId) {
        return "issued:set:coupon:" + couponId;
    }

    public static String reserveKey(Long couponId, Long userId) {
        return "reserve:coupon:" + couponId + ":user:" + userId;
    }

    public static String gateKey(Long couponId) {
        return "gate:coupon:" + couponId;
    }
}
