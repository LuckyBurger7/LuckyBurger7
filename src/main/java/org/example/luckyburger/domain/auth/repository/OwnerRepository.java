package org.example.luckyburger.domain.auth.repository;

import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OwnerRepository extends JpaRepository<Owner, Long> {

    @EntityGraph(attributePaths = {"shop"})
    Optional<Owner> findByAccount(Account account);
}
