package org.example.luckyburger.domain.shop.repository;

import org.example.luckyburger.domain.shop.entity.CouponPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ShopCouponRepository extends JpaRepository<CouponPolicy, Long> {
    @Query("select cp from CouponPolicy cp " +
            "where cp.shop.id = :shopId and cp.coupon.id = :couponId")
    Optional<CouponPolicy> findByShopIdAndCouponId(@Param("shopId") Long shopId,
                                                   @Param("couponId") Long couponId);

    @Modifying
    @Query(value = """
                INSERT IGNORE INTO coupon_policies (shop_id, coupon_id, status)
                SELECT s.id, :couponId, :status
                FROM shops s
            """, nativeQuery = true)
    void saveForAllShop(@Param("couponId") Long couponId, @Param("status") String status);

    @Modifying
    @Query(value = """
                INSERT IGNORE INTO coupon_policies (shop_id, coupon_id, status)
                SELECT :shopId, c.id, :status
                FROM coupons c
            """, nativeQuery = true)
    void saveForAllCoupon(@Param("shopId") Long shopId, @Param("status") String status);
}
