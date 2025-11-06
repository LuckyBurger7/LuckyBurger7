package org.example.luckyburger.domain.coupon.audit;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponAuditRepository extends JpaRepository<CouponAudit, Long> {
}
