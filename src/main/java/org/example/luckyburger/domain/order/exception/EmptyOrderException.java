package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class EmptyOrderException extends GlobalException {
    public EmptyOrderException() {
        super(OrderErrorCode.EMPTY_ORDER);
    }
}