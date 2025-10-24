package org.example.luckyburger.domain.shop.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShopErrorCode implements ErrorCode {

    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "상점을 찾을 수가 없습니다."),
    OWNER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "본인 점포에만 수행할 수 있는 작업입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
