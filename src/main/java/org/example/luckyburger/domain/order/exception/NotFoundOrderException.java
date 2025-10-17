package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class NotFoundOrderException extends GlobalException {
    public NotFoundOrderException() {
        super(OrderErrorCode.ORDER_NOT_FOUND);
    }
}