package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
                select o.id
                from Order o
                where o.user.id = :userId
                order by o.orderDate desc, o.id desc
            """)
    Page<Long> findIdsByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("""
                select o
                from Order o
                join fetch o.shop s
                where o.id in :ids
                order by o.orderDate desc, o.id desc
            """)
    List<Order> findWithShopByIdIn(@Param("ids") List<Long> ids);
    
    @EntityGraph(attributePaths = {"user", "shop"})
    List<Order> findAllByShopId(@Param("shopId") Long shopId);
}
