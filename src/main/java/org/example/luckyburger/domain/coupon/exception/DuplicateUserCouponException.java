package org.example.luckyburger.domain.coupon.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.coupon.code.UserCouponErrorCode;

public class DuplicateUserCouponException extends GlobalException {
    public DuplicateUserCouponException() {
        super(UserCouponErrorCode.DUPLICATE_USER_COUPON);
    }
}
