package org.example.luckyburger.common.security.dto;

import lombok.Getter;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthAccount {
    private final Long accountId;
    private final String email;
    private final AccountRole role;
    private final Collection<? extends GrantedAuthority> authorities;

    public AuthAccount(
            Long accountId,
            String email,
            AccountRole role) {
        this.accountId = accountId;
        this.email = email;
        this.role = role;
        this.authorities = List.of(new SimpleGrantedAuthority(role.getUserRole()));
    }
}
