package org.example.luckyburger.domain.shop.exception.shopMenu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ShopMenuErrorCode {

    SHOP_MENU_ERROR_CODE("SHOPMENU-001", HttpStatus.NOT_FOUND, "상점을 찾을 수가 없습니다.");


    private final String code;
    private final HttpStatus httpStatus;
    private final String message;
}
