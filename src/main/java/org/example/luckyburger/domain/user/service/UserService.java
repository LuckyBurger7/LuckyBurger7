package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다. id=" + userId));
    }
}
