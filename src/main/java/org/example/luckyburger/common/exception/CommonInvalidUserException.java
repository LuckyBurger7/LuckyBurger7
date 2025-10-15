package org.example.luckyburger.common.exception;

import org.example.luckyburger.common.code.CommonErrorCode;

public class CommonInvalidUserException extends GlobalException {
    
    public CommonInvalidUserException() {
        super(CommonErrorCode.INVALID_USER);
    }
}
