package org.example.luckyburger.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.user.entity.User;

@Getter
@Entity
@Table(name = "order_forms")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderForm extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_menu_id", nullable = false)
    private ShopMenu shopMenu;

    private int quantity;

    private OrderForm(User user, ShopMenu shopMenu, int quantity) {
        this.user = user;
        this.shopMenu = shopMenu;
        this.quantity = quantity;
    }

    public static OrderForm of(User user, ShopMenu shopMenu, int quantity) {
        return new OrderForm(user, shopMenu, quantity);
    }
}
