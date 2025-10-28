package org.example.luckyburger.domain.coupon.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Page<Coupon> findCouponsByExpirationDateAfter(LocalDateTime expirationDateAfter, Pageable pageable);

    Long countByExpirationDateAfter(LocalDateTime expirationDateAfter);

    // 더미 데이터 체크
    Optional<Coupon> findByName(String name);
}
