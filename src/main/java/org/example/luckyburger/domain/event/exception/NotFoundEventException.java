package org.example.luckyburger.domain.event.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.event.code.EventErrorcode;

public class NotFoundEventException extends GlobalException {
    public NotFoundEventException() {
        super(EventErrorcode.NOT_FOUND_EVENT);
    }
}
