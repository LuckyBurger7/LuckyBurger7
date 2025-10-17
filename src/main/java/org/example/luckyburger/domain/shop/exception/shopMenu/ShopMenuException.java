package org.example.luckyburger.domain.shop.exception.shopMenu;

import org.example.luckyburger.common.code.ErrorCode;
import org.example.luckyburger.common.exception.GlobalException;

public class ShopMenuException extends GlobalException {
  public ShopMenuException(ErrorCode errorCode) {
    super(errorCode);
  }
}
