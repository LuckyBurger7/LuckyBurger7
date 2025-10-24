package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.statistic.dto.response.MonthTotalSalesResponse;
import org.example.luckyburger.domain.statistic.dto.response.ShopTotalSalesResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"shop"})
    Page<Order> findByUserId(Long userId, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "shop"})
    List<Order> findAllByShopId(Long shopId);

    @EntityGraph(attributePaths = {"shop"})
    Page<Order> findByShop(Shop shop, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "shop"})
    Optional<Order> findById(Long orderId);

    long countByStatusNot(OrderStatus status);

    Long findSumOfTotalPriceByShop(Shop shop);

    @Query("""
                SELECT SUM(o.totalPrice)
                FROM Order o
                WHERE o.shop = :shop AND o.orderDate >= :orderDateAfter
            """)
    Long findSumOfTotalPriceByShopAndOrderDateAfter(Shop shop, LocalDateTime orderDateAfter);

    Long countByShopAndStatusAndOrderDateAfter(Shop shop, OrderStatus status, LocalDateTime orderDateAfter);

    Long countByStatus(OrderStatus status);

    @Query("""
                select coalesce(sum(o.totalPrice), 0)
                from Order o
                where o.shop.id = :shopId
                  and o.status = :status
                  and o.orderDate >= :start
                  and o.orderDate < :end
            """)
    Long sumMonthlyPaidByShopId(Long shopId, OrderStatus status, LocalDateTime start, LocalDateTime end);

    @Query(value = """
                SELECT
                    YEAR(o.order_date) AS year,
                    MONTH(o.order_date) AS month,
                    CAST(SUM(o.total_price) AS SIGNED) AS totalSales
                FROM
                    orders o
                WHERE
                    o.status = 'COMPLETED'
                GROUP BY
                    year,
                    month
                ORDER BY
                    year DESC,
                    month DESC
                LIMIT
                    6
            """, nativeQuery = true)
    List<MonthTotalSalesResponse> findAllMonthTotalSalesResponse();

    @Query(value = """
                SELECT
                    s.id AS shopId,
                    s.name AS shopName,
                    CAST(SUM(o.total_price) AS SIGNED) AS totalSales
                FROM
                    orders o
                JOIN
                    shops s ON o.shop_id = s.id
                WHERE
                    o.status = 'COMPLETED'
                GROUP BY
                    s.id,
                    s.name
                ORDER BY
                    totalSales DESC
                LIMIT
                    10
            """, nativeQuery = true)
    List<ShopTotalSalesResponse> findAllShopTotalSalesResponseOrderByDesc();

    @Query(value = """
                SELECT
                    s.id AS shopId,
                    s.name AS shopName,
                    CAST(SUM(o.total_price) AS SIGNED) AS totalSales
                FROM
                    orders o
                JOIN
                    shops s ON o.shop_id = s.id
                WHERE
                    o.status = 'COMPLETED'
                GROUP BY
                    s.id,
                    s.name
                ORDER BY
                    totalSales ASC
                LIMIT
                    10
            """, nativeQuery = true)
    List<ShopTotalSalesResponse> findAllShopTotalSalesResponseOrderByAsc();
}
