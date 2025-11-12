package org.example.luckyburger.common._dummyData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class InitDataSetting implements CommandLineRunner {

    private final AuthService authService;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // === 멱등 마커: 관리자 계정이 이미 있으면 전체 시딩 스킵 ===
        if (accountRepository.findByEmail("admin@naver.com").isPresent()) {
            log.info("[DummyDataLoader] admin@naver.com already exists. Skip seeding.");
            return;
        }

        // 1) 계정/유저/점주 ensure
        authService.createAccount(new AccountSignupRequest("admin@naver.com", "password", "관리자"), AccountRole.ROLE_ADMIN);
    }
}
