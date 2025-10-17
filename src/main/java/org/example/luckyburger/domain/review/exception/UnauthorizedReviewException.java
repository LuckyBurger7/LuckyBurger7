package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorcode;

public class UnauthorizedReviewException extends GlobalException {
    public UnauthorizedReviewException() {
        super(ReviewErrorcode.REVIEW_UNAUTHORIZED);
    }
}
