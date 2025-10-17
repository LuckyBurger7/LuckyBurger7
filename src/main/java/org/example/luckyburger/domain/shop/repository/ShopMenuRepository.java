package org.example.luckyburger.domain.shop.repository;

import jakarta.persistence.Id;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Id> {

    List<ShopMenu> findAllByShopId(Long shopId);

    ShopMenu findByMenuId(Long menuId);

}
