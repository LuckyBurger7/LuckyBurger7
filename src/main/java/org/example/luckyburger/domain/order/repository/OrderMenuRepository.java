package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.OrderMenu;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
    @EntityGraph(attributePaths = {"shopMenu", "shopMenu.menu"})
    List<OrderMenu> findAllByOrderId(Long orderId);

    @Query("""
            select om
            from OrderMenu om
            join fetch om.shopMenu sm
            join fetch sm.menu m
            where om.order.id in :orderIds
            """)
    List<OrderMenu> findAllByOrderIdsInWithMenu(@Param("orderIds") List<Long> orderIds);
}
