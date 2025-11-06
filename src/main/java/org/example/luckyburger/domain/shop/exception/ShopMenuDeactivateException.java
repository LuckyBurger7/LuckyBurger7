package org.example.luckyburger.domain.shop.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.shop.code.ShopMenuErrorCode;

public class ShopMenuDeactivateException extends GlobalException {
    public ShopMenuDeactivateException() {
        super(ShopMenuErrorCode.SHOP_MENU_DEACTIVATE);
    }
}
