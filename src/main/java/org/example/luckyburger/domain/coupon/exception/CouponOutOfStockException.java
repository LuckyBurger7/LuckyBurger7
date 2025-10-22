package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.CouponErrorCode;

public class CouponOutOfStockException extends GlobalException {
    public CouponOutOfStockException() {
        super(CouponErrorCode.COUPON_OUT_OF_STOCK);
    }
}
