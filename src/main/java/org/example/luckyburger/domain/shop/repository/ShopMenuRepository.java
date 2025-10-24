package org.example.luckyburger.domain.shop.repository;

import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.statistic.dto.response.MenuTotalSalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShopMenuRepository extends JpaRepository<ShopMenu, Long> {

    @EntityGraph(attributePaths = {"menu"})
    Page<ShopMenu> findByShopIdAndStatusIsNot(Long shopId, ShopMenuStatus status, Pageable pageable);

    @EntityGraph(attributePaths = {"shop"})
    Optional<ShopMenu> findWithShopByShopIdAndMenuId(Long shopId, Long menuId);

    @Query(value = """
                SELECT
                    m.id AS menuId,
                    m.name AS menuName,
                    CAST(SUM(sm.sales_volume) AS SIGNED) AS totalSales
                FROM
                    shop_menus sm
                JOIN
                    menus m ON sm.menu_id = m.id
                WHERE
                    m.id IN (:menuIds)
                GROUP BY
                    m.id,
                    m.name
                ORDER BY
                    totalSales DESC
            """, nativeQuery = true)
    List<MenuTotalSalesResponse> findAllMenuTotalSalesResponseByMenuIds(@Param("menuIds") List<Long> menuIds);

    @Query("""
                        SELECT SUM(sm.salesVolume)
                        FROM ShopMenu sm
            """)
    Long findSumOfSalesVolumes();
}
