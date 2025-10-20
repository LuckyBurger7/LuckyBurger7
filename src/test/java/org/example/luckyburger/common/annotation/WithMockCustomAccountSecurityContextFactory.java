package org.example.luckyburger.common.annotation;

import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.filter.JwtAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomAccountSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAccount> {


    @Override
    public SecurityContext createSecurityContext(WithMockCustomAccount annotation) {

        final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        final JwtAuthenticationToken jwtAuthenticationToken
                = new JwtAuthenticationToken(new AuthAccount(
                annotation.id(), annotation.email(), annotation.role()));

        securityContext.setAuthentication(jwtAuthenticationToken);

        return securityContext;
    }
}
