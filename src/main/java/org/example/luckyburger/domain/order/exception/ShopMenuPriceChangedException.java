package org.example.luckyburger.domain.order.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.order.code.OrderErrorCode;

public class ShopMenuPriceChangedException extends GlobalException {
    public ShopMenuPriceChangedException() {
        super(OrderErrorCode.SHOP_MENU_PRICE_CHANGED);
    }
}