package org.example.luckyburger.domain.menu.exception;

import org.example.luckyburger.common.exception.GlobalException;
import org.example.luckyburger.domain.menu.code.MenuErrorcode;

public class MenuNotFoundException extends GlobalException {
    public MenuNotFoundException() {
        super(MenuErrorcode.MENU_NOT_FOUND);
    }
}