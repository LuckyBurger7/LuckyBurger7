package org.example.luckyburger.domain.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;

@Getter
@Entity
@Table(name = "shop_menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopMenu extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private ShopMenuStatus status;

    @Column(name = "sales_volume")
    private long salesVolume;

    private ShopMenu(Shop shop, Menu menu, ShopMenuStatus status, long salesVolume) {
        this.shop = shop;
        this.menu = menu;
        this.status = status;
        this.salesVolume = salesVolume;
    }

    @Builder
    public static ShopMenu of(Shop shop, Menu menu, ShopMenuStatus status, long salesVolume) {
        return new ShopMenu(shop, menu, status, salesVolume);
    }

    public void updateShopMenuStatus(ShopMenuStatus status) {
        this.status = status;
    }

    public void increaseSalesVolume(long salesVolume) {
        this.salesVolume += salesVolume;
    }
}
