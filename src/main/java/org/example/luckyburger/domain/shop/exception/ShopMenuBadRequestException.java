package org.example.luckyburger.domain.shop.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.shop.code.ShopMenuErrorCode;

public class ShopMenuBadRequestException extends GlobalException {
    public ShopMenuBadRequestException() {
        super(ShopMenuErrorCode.SHOP_MENU_BAD_REQUEST);
    }
}
