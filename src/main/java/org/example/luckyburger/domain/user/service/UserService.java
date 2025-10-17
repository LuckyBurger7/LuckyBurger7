package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.security.utils.AuthAccountUtil;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.dto.response.AccountResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.dto.request.UserUpdateRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.entity.User;
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

        return UserResponse.from(userRepository.save(user), account);
    }

    @Transactional
    public UserResponse updateUser(UserUpdateRequest userRequest) {
        Account account = accountEntityFinder.getAccountById(AuthAccountUtil.getAuthAccount().accountId());

        User user = userEntityFinder.getUserByAccount(account);

        authService.updateAccount(userRequest.name());

        user.updateUser(userRequest.phone(), userRequest.address(), userRequest.street());

        return UserResponse.from(user, account);
    }
}
