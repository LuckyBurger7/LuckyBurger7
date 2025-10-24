package org.example.luckyburger.domain.shop.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.shop.code.ShopErrorCode;

public class OwnerUnauthorizedShopException extends GlobalException {
    public OwnerUnauthorizedShopException() {
        super(ShopErrorCode.OWNER_UNAUTHORIZED);
    }
}
