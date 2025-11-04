package org.example.luckyburger.domain.user.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class UserEntityFinder {

    private final UserCacheEntityFinder userCacheEntityFinder;

    public User getLoginUser() {
        return userCacheEntityFinder.getUserByAccountId(AuthAccountUtil.getAuthAccount().getAccountId());
    }
}
