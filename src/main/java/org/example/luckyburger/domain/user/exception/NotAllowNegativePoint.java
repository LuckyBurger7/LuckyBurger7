package org.example.luckyburger.domain.user.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.user.code.UserErrorCode;

public class NotAllowNegativePoint extends GlobalException {
    public NotAllowNegativePoint() {
        super(UserErrorCode.NOT_ALLOW_NEGATIVE_POINT);
    }
}