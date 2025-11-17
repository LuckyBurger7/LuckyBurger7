package org.example.luckyburger.common.security.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccessErrorCode implements ErrorCode {

    NO_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

}
