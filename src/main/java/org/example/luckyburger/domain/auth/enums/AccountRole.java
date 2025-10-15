package org.example.luckyburger.domain.auth.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountRole {

    ROLE_USER(Authority.USER),
    ROLE_OWNER(Authority.OWNER),
    ROLE_ADMIN(Authority.ADMIN);

    private final String userRole;

    // 오타 방지 및 @Secured를 위한 정의
    public static class Authority {
        public static final String USER = "ROLE_USER";
        public static final String OWNER = "ROLE_OWNER";
        public static final String ADMIN = "ROLE_ADMIN";
    }
}