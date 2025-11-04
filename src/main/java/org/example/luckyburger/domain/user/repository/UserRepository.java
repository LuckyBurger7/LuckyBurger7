package org.example.luckyburger.domain.user.repository;

import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "account")
    Optional<User> findById(Long accountId);

    boolean existsUserByPhone(String phone);
}
