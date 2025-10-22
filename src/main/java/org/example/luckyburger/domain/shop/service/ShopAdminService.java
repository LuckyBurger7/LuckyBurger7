package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.order.entity.Order;
import org.example.luckyburger.domain.order.service.OrderEntityFinder;
import org.example.luckyburger.domain.shop.dto.request.ShopRequest;
import org.example.luckyburger.domain.shop.dto.response.ShopResponse;
import org.example.luckyburger.domain.shop.entity.Shop;
import org.example.luckyburger.domain.shop.enums.BusinessStatus;
import org.example.luckyburger.domain.shop.repository.ShopRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopAdminService {

    private final ShopRepository shopRepository;
    private final ShopEntityFinder shopEntityFinder;
    private final OrderEntityFinder orderEntityFinder;

    @Transactional
    public ShopResponse createShop(ShopRequest shopRequest) {

        Shop shopEntity = Shop.of(shopRequest.getName(), shopRequest.getStatus(), shopRequest.getAddress(),shopRequest.getStreet());

        shopRepository.save(shopEntity);

        return ShopResponse.from(shopEntity);

    }

    // 상점의 상태를 변경
    @Transactional
    public Shop updateStatus(Long shopId,
                             BusinessStatus shopStatus) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        shopEntity.changeShop(shopStatus);

        return shopEntity;
    }

    // 상점의 정보를 수정
    @Transactional
    public ShopResponse updateShop(Long shopId, ShopRequest shopRequest) {

        Shop shopEntity = shopEntityFinder.getShopById(shopId);

        shopEntity.updateOfShop(shopRequest.getName(),shopRequest.getStatus(),shopRequest.getAddress(),shopRequest.getStreet());

        return ShopResponse.from(shopEntity);
    }

    // 상점 삭제 soft로 변경
    @Transactional
    public void deleteShop(Long shopId) {

        shopEntityFinder.getShopById(shopId).delete();

    }

    //점포 총 매출 조회
    @Transactional(readOnly = true)
    public int getTotal(Long shopId){

        int totalPrice = 0;

        List<Order> orderListByShop = orderEntityFinder.getAllOrderByShopId(shopId);

        for (Order order : orderListByShop) {
            totalPrice += order.getTotalPrice();
        }

        return totalPrice;
    }

}
