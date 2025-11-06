package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.CouponErrorCode;

public class CouponNotIssuedException extends GlobalException {
    public CouponNotIssuedException() {
        super(CouponErrorCode.COUPON_NOT_ISSUED);
    }
}
