package org.example.luckyburger.common._dummyData;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DummyDataLoader implements CommandLineRunner {
    private final UserService userService;
    private final AuthService authService;

    @Override
    @Transactional
    public void run(String... args) {
        authService.createAccount(new AccountSignupRequest(
                "admin@naver.com",
                "password",
                "관리자"
        ), AccountRole.ROLE_ADMIN);

        userService.createUser(new UserSignupRequest(
                "user1@naver.com",
                "password",
                "김기수",
                "010-3333-5555",
                "주소",
                "상세 주소"
        ));
        userService.createUser(new UserSignupRequest(
                "user2@naver.com",
                "password",
                "홍길동",
                "010-7777-8888",
                "주소",
                "상세 주소"
        ));

    }
}
