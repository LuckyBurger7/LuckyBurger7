package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.user.entity.User;
import org.example.luckyburger.domain.user.exception.UserNotFoundException;
import org.example.luckyburger.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserEntityFinder {

    private final UserRepository userRepository;

    /**
     * 계정으로 유저 조회 후 반환
     *
     * @param accountId 계정 Id
     * @return 유저 엔티티 반환
     */
    public User getUserByAccountId(Long accountId) {
        User user = userRepository.findById(accountId).orElseThrow(
                UserNotFoundException::new
        );

        if (user.getAccount().getDeletedAt() != null)
            throw new UserNotFoundException();

        return user;
    }
}
