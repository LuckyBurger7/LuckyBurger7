package org.example.luckyburger.common.exception;

import lombok.Getter;
import org.example.luckyburger.common.code.ErrorCode;

@Getter
public class GlobalException extends RuntimeException {
    
    private final ErrorCode errorCode;

    public GlobalException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
