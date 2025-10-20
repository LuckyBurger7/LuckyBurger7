package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorcode;

public class ReviewUnauthorizedException extends GlobalException {
    public ReviewUnauthorizedException() {
        super(ReviewErrorcode.REVIEW_UNAUTHORIZED);
    }
}
