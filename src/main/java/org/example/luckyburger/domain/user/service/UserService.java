package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.AccountUpdateRequest;
import org.example.luckyburger.domain.auth.dto.response.AccountResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.dto.request.UserUpdateRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.exception.DuplicatePhoneException;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AccountEntityFinder accountEntityFinder;
    private final UserEntityFinder userEntityFinder;
    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(UserSignupRequest request) {
        // 연락처 중복 확인
        validateDuplicatePhone(request.phone());

        AccountResponse accountResponse = authService.createAccount(
                AccountSignupRequest.builder()
                        .email(request.email())
                        .password(request.password())
                        .name(request.name())
                        .build(), AccountRole.ROLE_USER);

        Account account = accountEntityFinder.getAccountById(accountResponse.id());

        User user = User.of(
                account,
                request.phone(),
                request.address(),
                request.street()
        );

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse updateProfile(UserUpdateRequest userRequest) {
        Account account = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().accountId());

        User user = userEntityFinder.getUserByAccount(account);

        authService.updateAccount(AccountUpdateRequest.builder()
                .name(userRequest.name())
                .build());

        user.updateUser(userRequest.phone(), userRequest.address(), userRequest.street());

        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getProfile() {
        Account account = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().accountId());

        User user = userEntityFinder.getUserByAccount(account);

        return UserResponse.from(user);
    }

    /**
     * 연락처 중복 여부 검사
     *
     * @param phone 검사할 연락처
     */
    @Transactional(readOnly = true)
    public void validateDuplicatePhone(String phone) {
        if (userRepository.existsUserByPhone(phone))
            throw new DuplicatePhoneException();
    }
}
