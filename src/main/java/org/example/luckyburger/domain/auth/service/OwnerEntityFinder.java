package org.example.luckyburger.domain.auth.service;

import lombok.RequiredArgsConstructor;
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

    public Owner getOwnerByAccountId(Long accountId) {

        return ownerRepository.findById(accountId).orElseThrow(
                OwnerNotFoundException::new
        );
    }
}
