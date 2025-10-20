package org.example.luckyburger.common.security.filter;

import org.example.luckyburger.common.security.dto.AuthAccount;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthAccount authAccount;

    public JwtAuthenticationToken(AuthAccount authAccount) {
        super(authAccount.getAuthorities());
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
