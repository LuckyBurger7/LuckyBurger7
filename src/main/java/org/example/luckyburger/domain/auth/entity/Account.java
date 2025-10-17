package org.example.luckyburger.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.example.luckyburger.domain.auth.enums.AccountRole;

@Getter
@Entity
@Table(name = "accounts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account extends BaseEntity {

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AccountRole role;

    private Account(String email, String name, String password, AccountRole role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    @Builder
    public static Account of(String email, String name, String password, AccountRole role) {
        return new Account(email, name, password, role);
    }

    public void updateAccount(String name) {
        this.name = name;
    }
}
