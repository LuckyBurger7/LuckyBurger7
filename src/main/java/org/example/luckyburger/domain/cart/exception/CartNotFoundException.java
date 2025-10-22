package org.example.luckyburger.domain.cart.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.cart.code.CartErrorCode;

public class CartNotFoundException extends GlobalException {
    public CartNotFoundException() {
        super(CartErrorCode.CART_NOT_FOUND);
    }
}
