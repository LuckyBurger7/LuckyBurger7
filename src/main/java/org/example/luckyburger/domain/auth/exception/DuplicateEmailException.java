package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.AuthErrorCode;

public class DuplicateEmailException extends GlobalException {
    public DuplicateEmailException() {
        super(AuthErrorCode.DUPLICATE_EMAIL_ERROR);
    }
}
