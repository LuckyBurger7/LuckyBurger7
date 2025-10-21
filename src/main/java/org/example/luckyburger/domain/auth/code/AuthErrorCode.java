package org.example.luckyburger.domain.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthErrorCode implements ErrorCode {

    ACCOUNT_NOT_FOUND(HttpStatus.NOT_FOUND, "계정을 찾을 수 없습니다."),
    NO_AUTHORITY(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "인증 정보가 유효하지 않습니다."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "같은 이메일이 존재합니다."),
    OWNER_NOT_FOUND(HttpStatus.NOT_FOUND, "점주 계정을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
