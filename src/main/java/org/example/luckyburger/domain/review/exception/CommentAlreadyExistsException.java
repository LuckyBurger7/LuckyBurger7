package org.example.luckyburger.domain.review.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.review.code.ReviewErrorcode;

public class CommentAlreadyExistsException extends GlobalException {
    public CommentAlreadyExistsException() {
        super(ReviewErrorcode.COMMENT_ALREADY_EXISTS);
    }
}
