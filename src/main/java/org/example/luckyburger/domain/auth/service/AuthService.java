package org.example.luckyburger.domain.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.AccountUpdateRequest;
import org.example.luckyburger.domain.auth.dto.request.CredentialRequest;
import org.example.luckyburger.domain.auth.dto.request.LoginRequest;
import org.example.luckyburger.domain.auth.dto.response.AccountResponse;
import org.example.luckyburger.domain.auth.dto.response.TokenResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.exception.AccountNotFoundException;
import org.example.luckyburger.domain.auth.exception.AuthenticationFailedException;
import org.example.luckyburger.domain.auth.exception.DuplicateEmailException;
import org.example.luckyburger.domain.auth.exception.NoAuthorityException;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthService {

    private final AccountRepository accountRepository;
    private final AccountEntityFinder accountEntityFinder;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 계정 생성
     *
     * @param request 계정 가입 요청 DTO
     * @return 계정 응답 DTO
     */
    @Transactional
    public AccountResponse createAccount(AccountSignupRequest request, AccountRole accountRole) {
        // 이메일 중복 확인
        validateDuplicateEmail(request.email());

        String encodePassword = passwordEncoder.encode(request.password());

        Account account = Account.of(
                request.email(),
                request.name(),
                encodePassword,
                accountRole
        );

        return AccountResponse.from(accountRepository.save(account));
    }

    /**
     * 계정 수정
     *
     * @param request 계정 수정 응답 DTO
     * @return 계정 응답 DTO
     */
    @Transactional
    public AccountResponse updateAccount(AccountUpdateRequest request) {

        AuthAccount authAccount = AuthAccountUtil.getAuthAccount();

        Account account = accountEntityFinder.getAccountByEmail(authAccount.getEmail());

        account.updateAccount(request.name());

        return AccountResponse.from(account);
    }

    /**
     * 공용 로그인
     *
     * @param request 로그인 요청 DTO
     * @return 토큰 응답 DTO
     */
    @Transactional
    public TokenResponse login(LoginRequest request) {
        // 아이디 검사
        Account account = loadAccountForAuthentication(request.email());
        // 비밀번호 검사
        if (isMismatchedPassword(request.password(), account.getPassword()))
            throw new AuthenticationFailedException();

        String accessToken = jwtUtil.createToken(account.getId(), account.getEmail(), account.getRole());

        return TokenResponse.of(accessToken);
    }

    /**
     * 회원 탈퇴
     *
     * @param request 회원 탈퇴 요청 DTO
     */
    @Transactional
    public void withdraw(CredentialRequest request) {
        AuthAccount authAccount = AuthAccountUtil.getAuthAccount();

        Account account = accountEntityFinder.getAccountById(authAccount.getAccountId());

        // 비밀번호 검사
        if (isMismatchedPassword(request.password(), account.getPassword()))
            throw new AuthenticationFailedException();

        // 권한이 사용자인지 검사
        if (authAccount.getRole() != AccountRole.ROLE_USER)
            throw new NoAuthorityException();

        account.delete();
    }

    /**
     * 이메일 중복 여부 검사
     *
     * @param email 검사할 이메일
     */
    @Transactional(readOnly = true)
    public void validateDuplicateEmail(String email) {
        if (accountRepository.existsAccountByEmail(email))
            throw new DuplicateEmailException();
    }

    /**
     * 이메일 존재 여부 및 삭제 여부 확인 후 계정 반환
     *
     * @param email 검사할 이메일
     * @return 계정 반환
     * @throws AccountNotFoundException 인증 정보가 유효하지 않음
     */
    private Account loadAccountForAuthentication(String email) throws AccountNotFoundException {
        Account account = accountRepository.findByEmail(email).orElseThrow(
                AuthenticationFailedException::new);
        if (account.getDeletedAt() != null)
            throw new AuthenticationFailedException();

        return account;
    }

    /**
     * 비밀번호화 암호화된 비밀번호를 비교해 같지 않을때 true를 반환
     *
     * @param rawPassword    비밀번호
     * @param encodePassword 암호화된 비밀번호
     * @return 다르다면 true, 같다면 false
     */
    private boolean isMismatchedPassword(String rawPassword, String encodePassword) {
        return !passwordEncoder.matches(rawPassword, encodePassword);
    }


}
