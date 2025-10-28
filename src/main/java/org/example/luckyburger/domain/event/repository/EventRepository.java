package org.example.luckyburger.domain.event.repository;

import java.util.Optional;
import org.example.luckyburger.domain.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findAllByDeletedAtIsNull(Pageable pageable);

    // 더미 데이터 체크
    Optional<Event> findByTitle(String title);
}
