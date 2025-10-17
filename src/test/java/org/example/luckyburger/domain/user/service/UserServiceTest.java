package org.example.luckyburger.domain.user.service;

import org.example.luckyburger.common.annotation.WithMockCustomAccount;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.example.luckyburger.domain.auth.service.AccountEntityFinder;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.user.dto.request.UserSignupRequest;
import org.example.luckyburger.domain.user.dto.request.UserUpdateRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    final long ACCOUNT_ID = 100000L;

    @Autowired
    private AccountEntityFinder accountEntityFinder;
    @Autowired
    private UserEntityFinder userEntityFinder;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        UserSignupRequest request = UserSignupRequest.builder()
                .email("test@example.com")
                .password("oldPassword")
                .name("OldName")
                .phone("010-0000-0000")
                .address("경기도")
                .street("000-000")
                .build();
        Account account = Account.of(
                request.email(),
                request.name(),
                request.password(),
                AccountRole.ROLE_USER
        );
        accountRepository.save(account);

        User user = User.of(
                account,
                request.phone(),
                request.address(),
                request.street()
        );
        userRepository.save(user);
    }

    @Test
    @WithMockCustomAccount(id = ACCOUNT_ID, email = "test@example.com", role = AccountRole.ROLE_USER)
    void updateProfile() {
        // given
        UserUpdateRequest userUpdaterequest = UserUpdateRequest.builder()
                .name("NewName")
                .phone("010-1111-1111")
                .address("전라북도")
                .street("111-111")
                .build();

        // when
        UserResponse response = userService.updateProfile(userUpdaterequest);

        // then
        Assertions.assertEquals("010-1111-1111", response.phone());
    }
}