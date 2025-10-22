package org.example.luckyburger.domain.cart.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.cart.code.CartMenuErrorCode;

public class CartMenuForbiddenException extends GlobalException {
    public CartMenuForbiddenException() {
        super(CartMenuErrorCode.CART_MENU_FORBIDDEN);
    }
}
