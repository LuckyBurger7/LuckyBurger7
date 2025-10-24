package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorCode;

public class OwnerUnauthorizedAccessException extends GlobalException {
    public OwnerUnauthorizedAccessException() {
        super(ReviewErrorCode.OWNER_UNAUTHORIZED);
    }
}
