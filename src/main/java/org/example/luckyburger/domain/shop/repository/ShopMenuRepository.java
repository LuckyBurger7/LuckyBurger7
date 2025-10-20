package org.example.luckyburger.domain.shop.repository;

import jakarta.persistence.Id;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Id> {

    List<ShopMenu> getAllByShopId(Long shopId);

    ShopMenu getByMenuId(Long menuId);

    @Query("SELECT sm FROM ShopMenu sm JOIN FETCH sm.menu WHERE sm.shop.id = :shopId")
    Page<ShopMenu> getByShopId(@Param("shopId")Long shopId, PageRequest pageRequest);
}
