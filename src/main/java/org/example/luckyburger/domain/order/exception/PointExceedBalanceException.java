package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class PointExceedBalanceException extends GlobalException {
    public PointExceedBalanceException() {
        super(OrderErrorCode.POINT_EXCEEDS_BALANCE);
    }
}