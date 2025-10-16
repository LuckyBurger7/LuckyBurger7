package org.example.luckyburger.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.menu.enums.MenuCategory;

@Getter
@Entity
@Table(name = "menus")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu extends BaseIdEntity {

    @Column(length = 100, nullable = false)
    private String name;

    @Column(name = "category")
    @Enumerated(value = EnumType.STRING)
    private MenuCategory category;

    private long price;

    private Menu(String name, MenuCategory category, long price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }

    @Builder
    public static Menu of(String name, MenuCategory category, long price) {
        return new Menu(name, category, price);
    }

    public void updateMenu(String name, MenuCategory category, long price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
}
