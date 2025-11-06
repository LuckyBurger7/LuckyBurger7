package org.example.luckyburger.domain.coupon.repository;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Page<Coupon> findCouponsByExpirationDateAfter(LocalDateTime expirationDateAfter, Pageable pageable);

    Long countByExpirationDateAfter(LocalDateTime expirationDateAfter);

    // 더미 데이터 체크
    Optional<Coupon> findByName(String name);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Coupon c where c.id = :id")
    @QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
    Optional<Coupon> findByIdForUpdate(@Param("id") Long id);
}
