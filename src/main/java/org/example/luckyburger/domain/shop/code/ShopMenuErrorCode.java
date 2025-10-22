package org.example.luckyburger.domain.shop.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShopMenuErrorCode implements ErrorCode {

    SHOP_MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "상점 매뉴를 찾을 수가 없습니다."),
    SHOP_MENU_BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
