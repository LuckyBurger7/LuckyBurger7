package org.example.luckyburger.domain.shop.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.coupon.entity.Coupon;
import org.example.luckyburger.domain.shop.enums.CouponStatus;

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

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponStatus status;

    private CouponPolicy(Shop shop, Coupon coupon, CouponStatus status) {
        this.shop = shop;
        this.coupon = coupon;
        this.status = status;
    }

    @Builder
    public static CouponPolicy of(Shop shop, Coupon coupon, CouponStatus status) {
        return new CouponPolicy(shop, coupon, status);
    }

    public void updateCouponPolicy(CouponStatus status) {
        this.status = status;
    }
}
