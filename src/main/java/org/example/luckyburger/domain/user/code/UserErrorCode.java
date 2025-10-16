package org.example.luckyburger.domain.user.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {

    NOT_ALLOW_NEGATIVE_POINT(HttpStatus.BAD_REQUEST, "포인트는 양수만 입력할 수 있습니다."),
    NOT_ENOUGH_POINT(HttpStatus.BAD_REQUEST, "사용할 포인트가 충분하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
