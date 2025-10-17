package org.example.luckyburger.domain.shop.exception.shop;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShopErrorCode implements ErrorCode {

    SHOP_NOT_FOUND("SHOP-001", HttpStatus.NOT_FOUND, "상점을 찾을 수가 없습니다.");

    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
