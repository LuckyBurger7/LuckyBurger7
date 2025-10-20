package org.example.luckyburger.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseEntity;
import org.example.luckyburger.domain.coupon.enums.CouponType;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends BaseEntity {

    @Column(length = 100, nullable = false)
    private String name;

    private Double discount;

    private int count;

    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    @Column(name = "type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private CouponType type;

    private Coupon(
            String name,
            Double discount,
            int count,
            LocalDateTime expirationDate,
            CouponType type
    ) {
        this.name = name;
        this.discount = discount;
        this.count = count;
        this.expirationDate = expirationDate;
        this.type = type;
    }

    public static Coupon of(
            String name,
            Double discount,
            int count,
            LocalDateTime expirationDate,
            CouponType type
    ) {
        return new Coupon(name, discount, count, expirationDate, type);
    }

    public void updateCoupon(
            String name,
            Double discount,
            int count,
            LocalDateTime expirationDate,
            CouponType type) {
        this.name = name;
        this.discount = discount;
        this.count = count;
        this.expirationDate = expirationDate;
        this.type = type;
    }
}
