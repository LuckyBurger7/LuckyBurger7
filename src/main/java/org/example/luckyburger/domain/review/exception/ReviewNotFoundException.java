package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorcode;

public class ReviewNotFoundException extends GlobalException {
    public ReviewNotFoundException() {
        super(ReviewErrorcode.REVIEW_NOT_FOUND);
    }
}
