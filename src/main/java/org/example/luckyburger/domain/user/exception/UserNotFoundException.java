package org.example.luckyburger.domain.user.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.user.code.UserErrorCode;

public class UserNotFoundException extends GlobalException {
    public UserNotFoundException() {
        super(UserErrorCode.USER_NOT_FOUND);
    }
}
