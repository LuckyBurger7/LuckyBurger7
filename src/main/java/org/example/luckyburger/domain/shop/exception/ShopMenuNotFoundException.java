package org.example.luckyburger.domain.shop.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.shop.code.ShopMenuErrorCode;

public class ShopMenuNotFoundException extends GlobalException {
  public ShopMenuNotFoundException() {
    super(ShopMenuErrorCode.SHOP_MENU_ERROR_CODE);
  }
}
