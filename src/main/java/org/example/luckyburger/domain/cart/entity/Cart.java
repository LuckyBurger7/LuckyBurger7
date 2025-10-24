package org.example.luckyburger.domain.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.domain.user.entity.User;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {
    @Id
    @Column(name = "account_id")
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private User user;

    @Column(name = "total_price")
    private long totalPrice;

    private Cart(User user, long totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
    }

    public static Cart of(User user, long totalPrice) {
        return new Cart(user, totalPrice);
    }

    public void updateTotalPrice(long totalPrice) {
        this.totalPrice = totalPrice;
    }
}
