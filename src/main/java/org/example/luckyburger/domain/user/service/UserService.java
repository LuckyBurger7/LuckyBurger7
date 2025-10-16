package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.user.dto.request.UserRequest;
import org.example.luckyburger.domain.user.dto.response.UserResponse;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponse createUser(UserRequest request) {
        User user = User.of(
                request.account(),
                request.phone(),
                request.address(),
                request.street()
        );

        return UserResponse.from(userRepository.save(user));
    }
}
