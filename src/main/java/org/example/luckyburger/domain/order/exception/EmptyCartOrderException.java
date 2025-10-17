package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class EmptyCartOrderException extends GlobalException {
    public EmptyCartOrderException() {
        super(OrderErrorCode.EMPTY_CART);
    }
}