package org.example.luckyburger.domain.coupon.audit;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "coupon_audit")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CouponAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long couponId;
    private Long userId;

    @Enumerated(EnumType.STRING)
    private CouponIssueStatus couponIssueStatus;

    private String reason;

    private LocalDateTime createdAt;

    private CouponAudit(Long couponId, Long userId, CouponIssueStatus status, String reason) {
        this.couponId = couponId;
        this.userId = userId;
        this.couponIssueStatus = status;
        this.reason = reason;
        this.createdAt = LocalDateTime.now();
    }

    public static CouponAudit of(Long couponId, Long userId, CouponIssueStatus status, String reason) {
        return new CouponAudit(couponId, userId, status, reason);
    }
}
