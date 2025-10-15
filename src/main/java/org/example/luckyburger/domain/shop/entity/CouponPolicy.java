package org.example.luckyburger.domain.shop.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.coupon.entity.Coupon;

@Getter
@Entity
@Table(name = "coupon_policies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponPolicy extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(nullable = false)
    private Boolean available;

    private CouponPolicy(Shop shop, Coupon coupon, Boolean available) {
        this.shop = shop;
        this.coupon = coupon;
        this.available = available;
    }

    @Builder
    public static CouponPolicy of(Shop shop, Coupon coupon, Boolean available) {
        return new CouponPolicy(shop, coupon, available);
    }
}
