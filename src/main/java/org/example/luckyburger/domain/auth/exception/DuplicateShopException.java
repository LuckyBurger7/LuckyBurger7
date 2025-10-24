package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.OwnerErrorCode;

public class DuplicateShopException extends GlobalException {
    public DuplicateShopException() {
        super(OwnerErrorCode.DUPLICATE_SHOP);
    }
}
