package org.example.luckyburger.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.coupon.entity.Coupon;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "hold_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HoldCoupon extends BaseIdEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id", nullable = false)
    private Coupon coupon;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "used_date")
    private LocalDateTime usedDate;

    private HoldCoupon(User user, Coupon coupon, LocalDateTime issueDate) {
        this.user = user;
        this.coupon = coupon;
        this.issueDate = issueDate;
    }

    @Builder
    public static HoldCoupon create(User user, Coupon coupon, LocalDateTime issueDate) {
        return new HoldCoupon(user, coupon, issueDate);
    }

    public void useCoupon() {
        this.usedDate = LocalDateTime.now();
    }
}
