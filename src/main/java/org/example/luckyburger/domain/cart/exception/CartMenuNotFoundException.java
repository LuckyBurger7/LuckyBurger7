package org.example.luckyburger.domain.cart.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.cart.code.CartMenuErrorCode;

public class CartMenuNotFoundException extends GlobalException {
    public CartMenuNotFoundException() {
        super(CartMenuErrorCode.CART_MENU_NOT_FOUND);
    }
}
