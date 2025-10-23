package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.enums.OrderStatus;
import org.example.luckyburger.domain.shop.entity.Shop;
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

    Integer countByShopAndOrderDateAfter(Shop shop, LocalDateTime midnightToday);
}
