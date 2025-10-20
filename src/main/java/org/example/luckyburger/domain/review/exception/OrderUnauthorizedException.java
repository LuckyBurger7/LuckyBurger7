package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorcode;

public class OrderUnauthorizedException extends GlobalException {
    public OrderUnauthorizedException() {
        super(ReviewErrorcode.ORDER_UNAUTHORIZED);
    }
}
