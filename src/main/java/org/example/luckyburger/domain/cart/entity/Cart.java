package org.example.luckyburger.domain.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.user.entity.User;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "total_price")
    private long totalPrice;

    private Cart(User user, long totalPrice) {
        this.user = user;
        this.totalPrice = totalPrice;
    }

    @Builder
    public static Cart of(User user, long totalPrice) {
        return new Cart(user, totalPrice);
    }
}
