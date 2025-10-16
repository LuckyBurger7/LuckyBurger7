package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.AuthErrorCode;

public class AuthenticationFailedException extends GlobalException {
    public AuthenticationFailedException() {
        super(AuthErrorCode.AUTHENTICATION_FAILED);
    }
}
