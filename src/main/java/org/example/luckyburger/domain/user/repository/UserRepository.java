package org.example.luckyburger.domain.user.repository;

import jakarta.websocket.server.PathParam;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "account")
    @Query("SELECT a FROM Account a WHERE a.id = :accountId")
    Optional<User> findByIdDetails(@PathParam("accountId") Long accountId);

    boolean existsUserByPhone(String phone);
}
