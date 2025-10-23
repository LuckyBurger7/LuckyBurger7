package org.example.luckyburger.domain.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.exception.AccountNotFoundException;
import org.example.luckyburger.domain.auth.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@Transactional(readOnly = true)
public class AccountEntityFinder {

    private final AccountRepository accountRepository;


    /**
     * 아이디로 게정 조회 후 반환
     *
     * @param accountId 계정 아이디
     * @return 계정 엔티티 반환
     */
    public Account getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                AccountNotFoundException::new);

        if (account.getDeletedAt() != null)
            throw new AccountNotFoundException();

        return account;
    }


    /**
     * 이메일로 게정 조회 후 반환
     *
     * @param email 계정 이메일
     * @return 계정 엔티티 반환
     */
    public Account getAccountByEmail(String email) {
        Account account = accountRepository.findByEmail(email).orElseThrow(
                AccountNotFoundException::new);

        if (account.getDeletedAt() != null)
            throw new AccountNotFoundException();

        return account;
    }
}
