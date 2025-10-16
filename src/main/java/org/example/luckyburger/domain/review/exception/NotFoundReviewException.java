package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorcode;

public class NotFoundReviewException extends GlobalException {

    public NotFoundReviewException() {
        super(ReviewErrorcode.NOT_FOUND_REVIEW);
    }
}
