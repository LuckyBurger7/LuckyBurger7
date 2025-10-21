package org.example.luckyburger.domain.coupon.repository;

import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    Page<UserCoupon> findAllByUser(User user, Pageable pageable);

    Optional<UserCoupon> findByUserAndCoupon(User user, Coupon coupon);
}
