package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.CouponErrorCode;

public class CouponExpiredException extends GlobalException {
    public CouponExpiredException() {
        super(CouponErrorCode.COUPON_EXPIRED);
    }
}
