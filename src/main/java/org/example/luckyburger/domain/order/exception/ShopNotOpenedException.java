package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class ShopNotOpenedException extends GlobalException {
    public ShopNotOpenedException() {
        super(OrderErrorCode.SHOP_NOT_OPENED);
    }
}