package org.example.luckyburger.domain.shop.repository;

import java.util.Optional;
import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShopCouponRepository extends JpaRepository<CouponPolicy, Long> {
    @Query("select cp from CouponPolicy cp " +
            "where cp.shop.id = :shopId and cp.coupon.id = :couponId")
    Optional<CouponPolicy> findByShopIdAndCouponId(@Param("shopId") Long shopId,
                                                   @Param("couponId") Long couponId);
}
