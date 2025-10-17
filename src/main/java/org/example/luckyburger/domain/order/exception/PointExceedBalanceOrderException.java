package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class PointExceedBalanceOrderException extends GlobalException {
    public PointExceedBalanceOrderException() {
        super(OrderErrorCode.POINT_EXCEEDS_BALANCE);
    }
}