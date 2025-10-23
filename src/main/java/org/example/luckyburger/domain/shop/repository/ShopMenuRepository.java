package org.example.luckyburger.domain.shop.repository;

import java.util.Optional;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {

    @EntityGraph(attributePaths = {"menu"})
    Page<ShopMenu> findByShopIdAndStatusIsNot(Long shopId, ShopMenuStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"shop"})
    Optional<ShopMenu> findWithShopByShopIdAndMenuId(Long shopId, Long menuId);
}
