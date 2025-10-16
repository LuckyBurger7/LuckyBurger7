package org.example.luckyburger.domain.auth.repository;

import org.example.luckyburger.domain.auth.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsAccountByEmail(String email);
}
