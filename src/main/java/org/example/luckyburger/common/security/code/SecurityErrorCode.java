package org.example.luckyburger.common.security.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SecurityErrorCode implements ErrorCode {

    INVALID_HEADER(HttpStatus.BAD_REQUEST, "인증 토큰이 필요합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
