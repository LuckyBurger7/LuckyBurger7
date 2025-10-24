package org.example.luckyburger.domain.coupon.repository;

import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.coupon.entity.UserCoupon;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    Page<UserCoupon> findAllByUserId(Long userId, Pageable pageable);

    Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

    boolean existsByUserAndCoupon(User user, Coupon coupon);

    @Query("""
            select uc
            from UserCoupon uc
            join fetch uc.coupon c
            where uc.user.id = :userId
              and c.deletedAt is null
              and c.expirationDate > CURRENT_TIMESTAMP
              and uc.usedDate is null
            """)
    List<UserCoupon> findAvailableByUserId(@Param("userId") Long userId);
}
