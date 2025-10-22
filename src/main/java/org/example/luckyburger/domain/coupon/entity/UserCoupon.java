package org.example.luckyburger.domain.coupon.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.luckyburger.common.entity.BaseIdEntity;
import org.example.luckyburger.domain.coupon.exception.CouponExpiredException;
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
        if (this.coupon.getDeletedAt() != null // 쿠폰이 삭제됐는지
                || this.coupon.isExpired() // 쿠폰이 만료됐는지
                || this.usedDate != null) // 쿠폰이 사용됐는지
        {
            throw new CouponExpiredException();
        }

        this.usedDate = LocalDateTime.now();
    }

    public void restoreCoupon() {
        this.usedDate = null;
    }
}
