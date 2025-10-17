package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.AuthErrorCode;

public class NoAuthorityException extends GlobalException {
    public NoAuthorityException() {
        super(AuthErrorCode.NO_AUTHORITY);
    }
}
