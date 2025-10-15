package org.example.luckyburger.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.auth.entity.Account;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(length = 50, nullable = false, unique = true)
    private String phone;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String street;

    private int point;

    private User(Account account, String phone, String address, String street, int point) {
        this.account = account;
        this.phone = phone;
        this.address = address;
        this.street = street;
        this.point = point;
    }

    @Builder
    public static User of(Account account, String phone, String address, String street, int point) {
        return new User(account, phone, address, street, point);
    }
}
