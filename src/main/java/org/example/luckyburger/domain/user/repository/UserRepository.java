package org.example.luckyburger.domain.user.repository;

import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccount(Account account);
}
