package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.request.ShopUpdateRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopAdminService {

    private final ShopRepository shopRepository;
    private final ShopEntityFinder shopEntityFinder;
    private final OrderEntityFinder orderEntityFinder;

    @Transactional
    public ShopResponse createShop(ShopRequest request) {
        // 초기 매장 상태 Close
        Shop shopEntity = Shop.of(request.name(), BusinessStatus.CLOSED, request.address(), request.street());

        return ShopResponse.from(shopRepository.save(shopEntity));

    }

    // 상점의 상태를 변경
    @Transactional
    public Shop updateShopStatus(Long shopId,
                                 ShopUpdateRequest request) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        shopEntity.updateShopStatus(request.businessStatus());

        return shopEntity;
    }

    // 상점의 정보를 수정
    @Transactional
    public ShopResponse updateShop(Long shopId, ShopRequest request) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        shopEntity.updateShop(request.name(), request.address(), request.street());

        return ShopResponse.from(shopEntity);
    }

    // 상점 삭제
    @Transactional
    public void deleteShop(Long shopId) {

        shopEntityFinder.getShopById(shopId).delete();

    }

    // 점포 총 매출 조회
    @Transactional(readOnly = true)
    public Long getTotalPrice(Long shopId) {

        Shop shop = shopEntityFinder.getShopById(shopId);

        return orderEntityFinder.getTotalPrice(shop);
    }

    // 점포 총 갯수
    @Transactional(readOnly = true)
    public long getTotalStoreCount() {
        return shopRepository.count();
    }

}
