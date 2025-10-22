package org.example.luckyburger.domain.coupon.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserCouponErrorCode implements ErrorCode {

    DUPLICATE_USER_COUPON(HttpStatus.NOT_FOUND, "쿠폰은 한장만 발급받을 수 있습니다."),
    USER_COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "보유한 쿠폰을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
