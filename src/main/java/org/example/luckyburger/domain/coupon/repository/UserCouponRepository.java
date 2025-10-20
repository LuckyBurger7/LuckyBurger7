package org.example.luckyburger.domain.coupon.repository;

import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
}
