package org.example.luckyburger.domain.user.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.user.code.UserErrorCode;

public class NotEnoughPointException extends GlobalException {
    public NotEnoughPointException() {
        super(UserErrorCode.NOT_ENOUGH_POINT);
    }
}
