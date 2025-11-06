package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.CouponErrorCode;

public class CouponNotOpenedException extends GlobalException {
    public CouponNotOpenedException() {
        super(CouponErrorCode.COUPON_NOT_OPENED);
    }
}
