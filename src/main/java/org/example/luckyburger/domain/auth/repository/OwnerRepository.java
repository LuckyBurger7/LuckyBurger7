package org.example.luckyburger.domain.auth.repository;

import org.example.luckyburger.domain.auth.entity.Owner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OwnerRepository extends JpaRepository<Owner, Long> {
}
