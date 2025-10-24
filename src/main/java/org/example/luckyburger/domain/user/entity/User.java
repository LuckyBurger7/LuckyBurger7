package org.example.luckyburger.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.user.exception.NotAllowNegativePoint;
import org.example.luckyburger.domain.user.exception.NotEnoughPointException;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "account_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(length = 50, nullable = false, unique = true)
    private String phone;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(length = 100, nullable = false)
    private String street;

    private int point;

    private User(Account account, String phone, String address, String street) {
        this.account = account;
        this.phone = phone;
        this.address = address;
        this.street = street;
    }

    @Builder
    public static User of(Account account, String phone, String address, String street) {
        return new User(account, phone, address, street);
    }

    public void usePoint(int amount) {
        if (amount <= 0)
            throw new NotAllowNegativePoint();
        if (this.point < amount)
            throw new NotEnoughPointException();

        this.point -= amount;
    }

    public void accumulatePoint(int amount) {
        this.point += amount;
    }

    public void updateUser(String phone, String address, String street) {
        this.phone = phone;
        this.address = address;
        this.street = street;
    }
}
