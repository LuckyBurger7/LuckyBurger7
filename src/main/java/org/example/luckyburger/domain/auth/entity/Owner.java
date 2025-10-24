package org.example.luckyburger.domain.auth.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.domain.shop.entity.Shop;

@Getter
@Entity
@Table(name = "owners")
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Owner {
    @Id
    @Column(name = "account_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private Shop shop;

    private Owner(Account account, Shop shop) {
        this.account = account;
        this.shop = shop;
    }

    @Builder
    public static Owner of(Account account, Shop shop) {
        return new Owner(account, shop);
    }

    public void updateOwner(Shop shop) {
        this.shop = shop;
    }

    public void deleteShop() {
        this.shop = null;
    }
}
