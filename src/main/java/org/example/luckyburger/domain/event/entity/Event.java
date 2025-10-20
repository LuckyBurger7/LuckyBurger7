package org.example.luckyburger.domain.event.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "events")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String title;

    @Column(length = 255, nullable = false)
    private String description;

    private Event(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Builder
    public static Event of(String title, String description) {
        return new Event(title, description);
    }

    public void updateEvent(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
