package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class ShopMenuNotOnSaleException extends GlobalException {
    public ShopMenuNotOnSaleException() {
        super(OrderErrorCode.SHOP_MENU_NOT_ON_SALE);
    }
}