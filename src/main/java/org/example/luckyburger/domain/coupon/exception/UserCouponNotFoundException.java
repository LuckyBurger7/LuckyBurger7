package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.UserCouponErrorCode;

public class UserCouponNotFoundException extends GlobalException {
    public UserCouponNotFoundException() {
        super(UserCouponErrorCode.USER_COUPON_NOT_FOUND);
    }
}
