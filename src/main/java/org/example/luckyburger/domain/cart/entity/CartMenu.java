package org.example.luckyburger.domain.cart.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.shop.entity.ShopMenu;

@Getter
@Entity
@Table(name = "cart_menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartMenu extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_menu_id", nullable = false)
    private ShopMenu shopMenu;

    private int quantity;

    private CartMenu(Cart cart, ShopMenu shopMenu, int quantity) {
        this.cart = cart;
        this.shopMenu = shopMenu;
        this.quantity = quantity;
    }

    @Builder
    public static CartMenu of(Cart cart, ShopMenu shopMenu, int quantity) {
        return new CartMenu(cart, shopMenu, quantity);
    }
}
