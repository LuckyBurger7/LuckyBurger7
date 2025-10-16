package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.dto.request.SignupAccountRequest;
import org.example.luckyburger.domain.auth.dto.response.AccountResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.service.AuthService;
import org.example.luckyburger.domain.user.dto.request.SignupUserRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthService authService;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse createUser(SignupUserRequest request) {

        SignupAccountRequest signupAccountRequest = SignupAccountRequest.builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .build();

        AccountResponse accountResponse = authService.createAccount(signupAccountRequest, AccountRole.ROLE_USER);

        Account account = authService.getAccountById(accountResponse.id());

        User user = User.of(
                account,
                request.phone(),
                request.address(),
                request.street()
        );

        User savedUser = userRepository.save(user);

        return UserResponse.of(
                savedUser.getId(),
                account.getEmail(),
                account.getName(),
                savedUser.getPhone(),
                savedUser.getAddress(),
                savedUser.getStreet()
        );
    }

    @Transactional
    public UserResponse updateUser() {
        return null;
    }
}
