package org.example.luckyburger.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.entity.Account;
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
     * @param account 계정 엔티티
     * @return 유저 엔티티 반환
     */
    public User getUserByAccount(Account account) {
        User user = userRepository.findByAccount(account).orElseThrow(
                UserNotFoundException::new
        );

        if (account.getDeletedAt() != null)
            throw new UserNotFoundException();

        return user;
    }
}
