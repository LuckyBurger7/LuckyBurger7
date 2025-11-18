package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class OrderCacheSaveFailedException extends GlobalException {
    public OrderCacheSaveFailedException() {
        super(OrderErrorCode.ORDER_CACHE_SAVE_FAILED);
    }
}