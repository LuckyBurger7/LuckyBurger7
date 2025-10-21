package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.OrderForm;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {
    void deleteAllByUser(User user);

    @EntityGraph(attributePaths = {"shopMenu", "shopMenu.menu"})
    List<OrderForm> findAllByUser(User user);
}
