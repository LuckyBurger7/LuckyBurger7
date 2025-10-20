package org.example.luckyburger.domain.event.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.event.code.EventErrorCode;

public class NotFoundEventException extends GlobalException {
    public NotFoundEventException() {
        super(EventErrorCode.EVENT_NOT_FOUND);
    }
}
