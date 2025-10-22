package org.example.luckyburger.domain.shop.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.exception.ShopMenuNotFoundException;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ShopMenuService {

    private final ShopMenuRepository shopMenuRepository;

    //가게에 해당하는 메뉴를 변경
    @Transactional
    public Menu updateMenuStatus(Long shopId, Long menuId, ShopMenuStatus shopMenuStatus) {

        List<ShopMenu> getMenuList = shopMenuRepository.getAllByShopId(shopId);

        ShopMenu menu = getMenuList.stream()
                .filter(m -> m.getId().equals(menuId))
                .findFirst()
                .orElseThrow(() -> new ShopMenuNotFoundException());

        menu.changeShopMenuStatus(shopMenuStatus);

        Menu changedMenu = menu.getMenu();

        return changedMenu;

    }

}
