package org.example.luckyburger.domain.coupon.code;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;
@Getter
@RequiredArgsConstructor
public enum CouponErrorCode implements ErrorCode{
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "쿠폰을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}


