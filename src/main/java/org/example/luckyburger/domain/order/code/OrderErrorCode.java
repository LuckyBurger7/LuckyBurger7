package org.example.luckyburger.domain.order.code;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OrderErrorCode implements ErrorCode {

    EMPTY_ORDER(HttpStatus.BAD_REQUEST, "장바구니에 담긴 메뉴가 없습니다."),
    POINT_EXCEEDS_BALANCE(HttpStatus.BAD_REQUEST, "사용 가능한 적립금 금액을 초과합니다."),
    NEGATIVE_PAY(HttpStatus.BAD_REQUEST, "결제 금액은 0원 이상이어야 합니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
    UNAUTHORIZED_ORDER_ACCESS(HttpStatus.UNAUTHORIZED, "해당 주문에 접근할 권한이 없습니다."),
    UNAUTHORIZED_CART_ACCESS(HttpStatus.UNAUTHORIZED, "해당 장바구니에 접근할 권한이 없습니다."),
    SHOP_NOT_OPENED(HttpStatus.BAD_REQUEST, "주문 가능한 영업 시간이 아닙니다."),
    ORDER_NOT_CANCELABLE(HttpStatus.BAD_REQUEST, "취소 가능한 주문이 아닙니다."),
    ORDER_STATUS_INVALID_TRANSITION(HttpStatus.BAD_REQUEST, "유효한 주문 상태 변경이 아닙니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
