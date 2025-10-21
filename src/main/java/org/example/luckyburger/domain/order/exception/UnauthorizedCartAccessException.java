package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class UnauthorizedCartAccessException extends GlobalException {
    public UnauthorizedCartAccessException() {
        super(OrderErrorCode.UNAUTHORIZED_CART_ACCESS);
    }
}