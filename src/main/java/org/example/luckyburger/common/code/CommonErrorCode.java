package org.example.luckyburger.common.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode {

    INVALID_USER(HttpStatus.BAD_REQUEST, "존재하지 않은 회원입니다."),
    INVALID_REVIEW(HttpStatus.NOT_FOUND, "존재하지 않는 리뷰입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
