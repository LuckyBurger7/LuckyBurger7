package org.example.luckyburger.common.security.utils;

import org.example.luckyburger.common.security.dto.AuthAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthAccountUtil {

    public static AuthAccount getAuthAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (AuthAccount) authentication.getPrincipal();
    }
}
