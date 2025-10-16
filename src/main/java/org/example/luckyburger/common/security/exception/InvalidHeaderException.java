package org.example.luckyburger.common.security.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.common.security.code.SecurityErrorCode;

public class InvalidHeaderException extends GlobalException {
    public InvalidHeaderException() {
        super(SecurityErrorCode.INVALID_HEADER);
    }
}
