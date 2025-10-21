package org.example.luckyburger.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.auth.exception.OwnerNotFoundException;
import org.example.luckyburger.domain.auth.repository.OwnerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OwnerEntityFinder {

    private final OwnerRepository ownerRepository;

    public Owner getOwnerByAccount(Account account) {
        Owner owner = ownerRepository.findByAccount(account).orElseThrow(
                OwnerNotFoundException::new
        );

        return owner;
    }
}
