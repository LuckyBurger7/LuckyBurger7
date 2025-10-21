package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.AuthErrorCode;

public class OwnerNotFoundException extends GlobalException {
    public OwnerNotFoundException() {
        super(AuthErrorCode.OWNER_NOT_FOUND);
    }
}
