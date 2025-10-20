package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.CouponErrorCode;

public class CouponNotFoundException extends GlobalException {
    public CouponNotFoundException(){super(CouponErrorCode.COUPON_NOT_FOUND);}
}
