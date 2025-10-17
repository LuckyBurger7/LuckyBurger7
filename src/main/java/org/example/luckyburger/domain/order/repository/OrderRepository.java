package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
