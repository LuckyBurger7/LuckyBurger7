package org.example.luckyburger.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.dto.AuthAccount;
import org.example.luckyburger.common.security.utils.JwtUtil;
import org.example.luckyburger.domain.auth.dto.request.LoginRequest;
import org.example.luckyburger.domain.auth.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.WithdrawRequest;
import org.example.luckyburger.domain.auth.dto.response.TokenResponse;
import org.example.luckyburger.domain.auth.dto.response.UserAccountResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.exception.AccountNotFoundException;
import org.example.luckyburger.domain.auth.exception.AuthenticationFailedException;
import org.example.luckyburger.domain.auth.exception.DuplicateEmailException;
import org.example.luckyburger.domain.auth.exception.NoPermissionException;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.user.dto.request.UserRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * 사용자 회원 가입
     *
     * @param request 사용자 회원 가입 요청 DTO
     * @return 사용자 계정 응답 DTO
     */
    @Transactional
    public UserAccountResponse userSignup(UserSignupRequest request) {
        // 이메일 중복 확인
        if (accountRepository.existsAccountByEmail(request.email()))
            throw new DuplicateEmailException();

        String encodePassword = passwordEncoder.encode(request.password());

        Account account = Account.of(
                request.email(),
                request.name(),
                encodePassword,
                AccountRole.ROLE_USER
        );

        Account savedAccount = accountRepository.save(account);

        UserRequest userRequest = UserRequest.of(
                savedAccount,
                request.phone(),
                request.address(),
                request.street()
        );

        UserResponse userResponse = userService.createUser(userRequest);

        return UserAccountResponse.of(
                savedAccount.getEmail(),
                savedAccount.getName(),
                userResponse.phone(),
                userResponse.address(),
                userResponse.street()
        );
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
    public void withdraw(WithdrawRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthAccount authAccount = (AuthAccount) authentication.getPrincipal();
        Account account = getAccountById(authAccount.accountId());
        // 유저 권한 검사
        if (authAccount.role() != AccountRole.ROLE_USER)
            throw new NoPermissionException();
        // 비밀번호 검사
        if (isMismatchedPassword(request.password(), account.getPassword()))
            throw new AuthenticationFailedException();

        account.delete();
    }

    /**
     * 아이디로 게정 조회해 계정 반환
     *
     * @param accountId 계정 아이디
     * @return 계정 엔티티 반환
     */
    private Account getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                AccountNotFoundException::new);

        if (account.getDeletedAt() != null)
            throw new AccountNotFoundException();

        return account;
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
