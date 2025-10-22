package org.example.luckyburger.domain.cart.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CartMenuErrorCode implements ErrorCode {

    CART_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
    CART_MENU_BAD_REQUEST(HttpStatus.BAD_REQUEST, "다른 가게의 메뉴는 같이 담을 수 없습니다."),
    CART_MENU_FORBIDDEN(HttpStatus.FORBIDDEN, "메뉴를 삭제할 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
