package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class OrderStatusInvalidUpdateException extends GlobalException {
    public OrderStatusInvalidUpdateException() {
        super(OrderErrorCode.ORDER_STATUS_INVALID_TRANSITION);
    }
}