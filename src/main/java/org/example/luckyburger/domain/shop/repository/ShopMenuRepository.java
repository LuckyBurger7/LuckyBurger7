package org.example.luckyburger.domain.shop.repository;

import jakarta.websocket.server.PathParam;
import org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.statistic.dto.response.MenuTotalSalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query(value = """
                INSERT IGNORE INTO shop_menus (shop_id, menu_id, status, sales_volume)
                SELECT s.id, :menuId, :status, 0 
                FROM shops s 
            """, nativeQuery = true)
    void saveForAllShop(@Param("menuId") Long menuId, @Param("status") String status);

    @Modifying
    @Query(value = """
                INSERT IGNORE INTO shop_menus (shop_id, menu_id, status, sales_volume)
                SELECT :shopId, m.id, :status, 0 
                FROM menus m 
            """, nativeQuery = true)
    void saveForAllMenu(@Param("shopId") Long shopId, @Param("status") String status);

    @EntityGraph(attributePaths = {"menu", "shop"})
    @Query("SELECT sm FROM ShopMenu sm WHERE sm.id = :shopMenuId")
    Optional<ShopMenu> findByIdWithDetails(@PathParam("shopMenuId") Long shopMenuId);

    @Query("""
            SELECT new org.example.luckyburger.domain.shop.dto.response.ShopMenuCacheResponse(s.id, m.id, m.price, sm.status)
            FROM ShopMenu sm JOIN sm.menu m JOIN sm.shop s
            WHERE sm.id = :shopMenuId
            """)
    Optional<ShopMenuCacheResponse> findShopMenuCacheResponseById(@Param("shopMenuId") Long shopMenuId);

    @Query("""
            SELECT sm FROM ShopMenu sm
            JOIN FETCH sm.menu
            JOIN FETCH sm.shop
            WHERE sm.id IN :ids
            """)
    List<ShopMenu> findAllById(List<Long> ids);

    @EntityGraph(attributePaths = {"menu"})
    @Query("SELECT sm FROM ShopMenu sm WHERE sm.shop = :shop")
    Page<ShopMenu> findAllByShop(@Param("shop") Shop shop, Pageable pageable);
}
