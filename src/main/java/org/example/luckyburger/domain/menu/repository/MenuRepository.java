package org.example.luckyburger.domain.menu.repository;

import java.util.List;
import java.util.Optional;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Query("SELECT m.id FROM Menu m WHERE m.category=:category")
    List<Long> findAllByCategory(@Param("menuCategory") MenuCategory category);

    // 더미 데이터 체크
    Optional<Menu> findByName(String name);
}
