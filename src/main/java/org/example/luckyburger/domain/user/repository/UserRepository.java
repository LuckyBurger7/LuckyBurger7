package org.example.luckyburger.domain.user.repository;

import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByAccount(Account account);

    boolean existsUserByPhone(String phone);

    @Query("""
                    SELECT u
                    FROM User u
                    WHERE u.account.id = :account_id
            """)
    Optional<User> findByAccountId(@Param("account_id") Long accountId);
}
