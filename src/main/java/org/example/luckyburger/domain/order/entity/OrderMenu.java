package org.example.luckyburger.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.shop.entity.ShopMenu;

@Getter
@Entity
@Table(name = "order_menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderMenu extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_menu_id", nullable = false)
    private ShopMenu shopMenu;

    private int quantity;

    private OrderMenu(Order order, ShopMenu shopMenu, int quantity) {
        this.order = order;
        this.shopMenu = shopMenu;
        this.quantity = quantity;
    }

    @Builder
    public static OrderMenu of(Order order, ShopMenu shopMenu, int quantity) {
        return new OrderMenu(order, shopMenu, quantity);
    }
}
