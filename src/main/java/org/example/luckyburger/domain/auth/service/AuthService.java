package org.example.luckyburger.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.auth.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.auth.dto.response.AuthResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public AuthResponse userSignup(UserSignupRequest request) {
        // TODO: 커스텀 예외로 수정
        if (accountRepository.existsAccountByEmail(request.email()))
            throw new IllegalArgumentException("이메일 중복");

        String encodePassword = passwordEncoder.encode(request.password());

        Account account = Account.of(
                request.email(),
                request.name(),
                encodePassword,
                AccountRole.ROLE_USER
        );
        Account savedAccount = accountRepository.save(account);
        String accessToken = jwtUtil.createToken(savedAccount.getId(), savedAccount.getEmail(), savedAccount.getRole());

        return AuthResponse.of(accessToken);
    }

    @Transactional
    public String loginTest() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthAccount authAccount = (AuthAccount) authentication.getPrincipal();

        return authAccount.rule().name();
    }
}
