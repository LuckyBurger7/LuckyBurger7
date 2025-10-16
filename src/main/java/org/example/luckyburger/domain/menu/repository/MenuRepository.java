package org.example.luckyburger.domain.menu.repository;

import org.example.luckyburger.domain.menu.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}
