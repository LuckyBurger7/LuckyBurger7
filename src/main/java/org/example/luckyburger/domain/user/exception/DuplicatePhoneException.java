package org.example.luckyburger.domain.user.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.user.code.UserErrorCode;

public class DuplicatePhoneException extends GlobalException {
    public DuplicatePhoneException() {
        super(UserErrorCode.DUPLICATE_PHONE);
    }
}
