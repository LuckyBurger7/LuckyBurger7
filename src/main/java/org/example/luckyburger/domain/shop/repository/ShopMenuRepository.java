package org.example.luckyburger.domain.shop.repository;

import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {

    List<ShopMenu> getAllByShopId(Long shopId);

    @EntityGraph(attributePaths = {"menu"})
    Page<ShopMenu> findByShopIdAndStatusIsNot(Long shopId, ShopMenuStatus status, Pageable pageable);
}
