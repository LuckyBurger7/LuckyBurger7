package org.example.luckyburger.domain.shop.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.shop.code.ShopErrorCode;

public class ShopNotFoundException extends GlobalException {

    public ShopNotFoundException() {
        super(ShopErrorCode.SHOP_NOT_FOUND);
    }
}
