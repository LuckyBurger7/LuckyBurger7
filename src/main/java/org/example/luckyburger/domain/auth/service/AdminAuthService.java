package org.example.luckyburger.domain.auth.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.auth.dto.request.AccountSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.OwnerSignupRequest;
import org.example.luckyburger.domain.auth.dto.request.OwnerUpdateRequest;
import org.example.luckyburger.domain.auth.dto.response.AccountResponse;
import org.example.luckyburger.domain.auth.dto.response.OwnerResponse;
import org.example.luckyburger.domain.auth.entity.Account;
import org.example.luckyburger.domain.auth.entity.Owner;
import org.example.luckyburger.domain.auth.enums.AccountRole;
import org.example.luckyburger.domain.auth.exception.DuplicateShopException;
import org.example.luckyburger.domain.auth.exception.OwnerNotFoundException;
import org.example.luckyburger.domain.auth.repository.OwnerRepository;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.service.ShopEntityFinder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminAuthService {

    private final OwnerRepository ownerRepository;
    private final AuthService authService;
    private final ShopEntityFinder shopEntityFinder;
    private final AccountEntityFinder accountEntityFinder;

    @Transactional
    public OwnerResponse createOwner(OwnerSignupRequest request) {

        Shop shop = shopEntityFinder.getShopById(request.shopId());

        // 지점 중복 확인
        validateDuplicateShop(shop);

        AccountResponse accountResponse = authService.createAccount(
                AccountSignupRequest.builder()
                        .email(request.email())
                        .password(request.password())
                        .name(request.name())
                        .build(), AccountRole.ROLE_OWNER);

        Account account = accountEntityFinder.getAccountById(accountResponse.id());

        Owner owner = Owner.of(
                account,
                shop
        );

        return OwnerResponse.from(ownerRepository.save(owner));
    }

    @Transactional(readOnly = true)
    public Page<OwnerResponse> getAllOwnerResponse(Pageable pageable) {
        Page<Owner> ownerPage = ownerRepository.findAllByShopNotNull(pageable);

        return ownerPage.map(OwnerResponse::from);
    }

    @Transactional
    public OwnerResponse updateOwner(Long accountId, OwnerUpdateRequest request) {
        Shop shop = shopEntityFinder.getShopById(request.shopId());

        validateDuplicateShop(shop);

        Owner owner = ownerRepository.findById(accountId).orElseThrow(OwnerNotFoundException::new);

        owner.updateOwner(shop);

        return OwnerResponse.from(owner);
    }

    @Transactional
    public void deleteOwner(Long accountId) {
        Owner owner = ownerRepository.findById(accountId).orElseThrow(OwnerNotFoundException::new);
        owner.getAccount().delete();
        owner.deleteShop();
    }

    private void validateDuplicateShop(Shop shop) {
        if (ownerRepository.existsOwnerByShop(shop))
            throw new DuplicateShopException();
    }
}
