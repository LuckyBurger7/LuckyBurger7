package org.example.luckyburger.domain.order.repository;

import org.example.luckyburger.domain.order.entity.OrderForm;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderFormRepository extends JpaRepository<OrderForm, Long> {

    @Modifying
    @Query("delete from OrderForm o where o.user = :user")
    void deleteByUser(User user);

    @EntityGraph(attributePaths = {"shopMenu", "shopMenu.menu"})
    List<OrderForm> findAllByUser(User user);
}
