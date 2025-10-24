package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorCode;

public class DuplicateReviewException extends GlobalException {
    public DuplicateReviewException() {
        super(ReviewErrorCode.REVIEW_ALREADY_EXISTS);
    }
}
