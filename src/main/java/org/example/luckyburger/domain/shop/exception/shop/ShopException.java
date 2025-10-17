package org.example.luckyburger.domain.shop.exception.shop;

import org.example.luckyburger.common.code.ErrorCode;
import org.example.luckyburger.common.exception.GlobalException;

public class ShopException extends GlobalException {

    public ShopException(ErrorCode errorCode) {
        super(errorCode);
    }
}
