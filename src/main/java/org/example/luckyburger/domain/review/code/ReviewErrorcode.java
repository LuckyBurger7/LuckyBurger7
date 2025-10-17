package org.example.luckyburger.domain.review.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorcode implements ErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    REVIEW_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "본인이 작성한 리뷰가 아닙니다."),
    COMMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 댓글이 존재 합니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
