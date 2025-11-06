package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.CouponErrorCode;

public class CouponCountLessThanIssuedException extends GlobalException {
    public CouponCountLessThanIssuedException() {
        super(CouponErrorCode.COUPON_COUNT_LESS_THAN_ISSUED);
    }
}
