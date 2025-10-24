package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorCode;

public class OrderUnauthorizedException extends GlobalException {
    public OrderUnauthorizedException() {
        super(ReviewErrorCode.ORDER_UNAUTHORIZED);
    }
}
