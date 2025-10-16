package org.example.luckyburger.domain.auth.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.auth.code.AuthErrorCode;

public class NoPermissionException extends GlobalException {
    public NoPermissionException() {
        super(AuthErrorCode.NO_PERMISSION);
    }
}
