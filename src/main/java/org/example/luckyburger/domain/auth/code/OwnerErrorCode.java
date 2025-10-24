package org.example.luckyburger.domain.auth.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OwnerErrorCode implements ErrorCode {
    DUPLICATE_SHOP(HttpStatus.BAD_REQUEST, "같은 점포가 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
