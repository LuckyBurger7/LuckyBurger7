package org.example.luckyburger.domain.coupon.repository;

import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Page<Coupon> findCouponsByExpirationDateAfter(LocalDateTime expirationDateBefore, Pageable pageable);

    Page<Coupon> findAll(Pageable pageable);
}
