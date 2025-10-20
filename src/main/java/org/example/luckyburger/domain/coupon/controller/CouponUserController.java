package org.example.luckyburger.domain.coupon.controller;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.coupon.service.CouponUserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Secured(AccountRole.Authority.USER)
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CouponUserController {

    private final CouponUserService couponUserService;

    
}
