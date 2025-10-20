package org.example.luckyburger.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.user.entity.User;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "user_coupons")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserCoupon extends BaseIdEntity {

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

    private UserCoupon(User user, Coupon coupon, LocalDateTime issueDate) {
        this.user = user;
        this.coupon = coupon;
        this.issueDate = issueDate;
    }

    public static UserCoupon of(User user, Coupon coupon, LocalDateTime issueDate) {
        return new UserCoupon(user, coupon, issueDate);
    }

    public void useCoupon() {
        this.usedDate = LocalDateTime.now();
    }
}
