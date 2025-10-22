package org.example.luckyburger.domain.cart.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.cart.code.CartMenuErrorCode;

public class CartMenuBadRequestException extends GlobalException {
    public CartMenuBadRequestException() {
        super(CartMenuErrorCode.CART_MENU_BAD_REQUEST);
    }
}
