package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.AuthErrorCode;

public class AccountNotFoundException extends GlobalException {
    public AccountNotFoundException() {
        super(AuthErrorCode.ACCOUNT_NOT_FOUND);
    }
}
