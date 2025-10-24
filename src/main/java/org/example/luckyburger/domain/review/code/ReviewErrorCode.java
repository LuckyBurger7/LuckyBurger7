package org.example.luckyburger.domain.review.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    REVIEW_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "본인이 작성한 리뷰가 아닙니다."),
    COMMENT_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 댓글이 존재 합니다."),
    ORDER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "본인이 주문한 제품이 아닙니다."),
    REVIEW_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "해당 주문에는 이미 리뷰가 존재합니다."),
    OWNER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "본인 점포에만 수행할 수 있는 작업입니다.");


    private final HttpStatus httpStatus;
    private final String message;
}
