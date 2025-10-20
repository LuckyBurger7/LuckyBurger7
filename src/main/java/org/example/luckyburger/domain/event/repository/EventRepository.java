package org.example.luckyburger.domain.event.repository;

import org.example.luckyburger.domain.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
