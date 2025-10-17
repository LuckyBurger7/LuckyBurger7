package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class NegativePayOrderException extends GlobalException {
    public NegativePayOrderException() {
        super(OrderErrorCode.NEGATIVE_PAY);
    }
}