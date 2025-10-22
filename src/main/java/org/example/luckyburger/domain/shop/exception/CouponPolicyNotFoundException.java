package org.example.luckyburger.domain.shop.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.shop.code.CouponPolicyErrorCode;

public class CouponPolicyNotFoundException extends GlobalException {
    public CouponPolicyNotFoundException() {
        super(CouponPolicyErrorCode.COUPON_POLICY_NOT_FOUND);

    }
}
