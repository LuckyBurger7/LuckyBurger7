package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class OrderNotCancelableException extends GlobalException {
    public OrderNotCancelableException() {
        super(OrderErrorCode.ORDER_NOT_CANCELABLE);
    }
}