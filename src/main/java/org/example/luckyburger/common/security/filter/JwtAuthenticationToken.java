package org.example.luckyburger.common.security.filter;

import org.example.luckyburger.common.security.dto.AuthAccount;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthAccount authAccount;

    public JwtAuthenticationToken(AuthAccount authAccount) {
        super(Collections.emptyList());
        this.authAccount = authAccount;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return authAccount;
    }
}
