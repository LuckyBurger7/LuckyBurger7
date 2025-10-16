package org.example.luckyburger.domain.review.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorcode implements ErrorCode {
    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "본인이 작성한 리뷰가 아닙니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
