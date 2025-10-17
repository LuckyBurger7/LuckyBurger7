package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class ShopNotOpenedOrderException extends GlobalException {
    public ShopNotOpenedOrderException() {
        super(OrderErrorCode.SHOP_NOT_OPENED);
    }
}