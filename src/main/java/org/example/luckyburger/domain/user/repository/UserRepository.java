package org.example.luckyburger.domain.user.repository;

import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    
}
