package org.example.luckyburger.domain.shop.service;

import lombok.AllArgsConstructor;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.exception.shopMenuCode.ShopMenuErrorCode;
import org.example.luckyburger.domain.shop.exception.shopMenuCode.ShopMenuException;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ShopMenuService {

    private final ShopMenuRepository shopMenuRepository;

    //MenuResponse로 반환하도록 변경하기
    @Transactional(readOnly = true)
    public Page<Menu> getShopMenuList(Long shopId,int page,int size){

        PageRequest pageable = PageRequest.of(page, size);

        Page<ShopMenu> byShopId = shopMenuRepository.getByShopId(shopId, pageable);

        Page<Menu> map = byShopId.map(ShopMenu::getMenu);

        return map;
    }

    @Transactional(readOnly = true)
    public MenuResponse getMenuDetail(Long shopId, Long menuId){

        List<ShopMenu> getMenuList = shopMenuRepository.getAllByShopId(shopId);

        //shopId 검증 필터 추가
        ShopMenu getShopMenu = getMenuList.stream()
                .filter(m -> m.getShop().getId().equals(shopId))
                .filter(m-> m.getMenu().getId().equals(menuId))
                .findFirst()
                .orElseThrow(()->new ShopMenuException(ShopMenuErrorCode.SHOP_MENU_ERROR_CODE));

        ShopMenu shopMenu = ShopMenu.of(getShopMenu.getShop(),
                getShopMenu.getMenu(),
                getShopMenu.getStatus(),
                getShopMenu.getSalesVolume());

        Menu menu = shopMenu.getMenu();

        return MenuResponse.from(menu);
    }

    //가게에 해당하는 메뉴를 변경
    @Transactional
    public Menu updateMenuStatus(Long shopId, Long menuId,ShopMenuStatus shopMenuStatus){

        List<ShopMenu> getMenuList = shopMenuRepository.getAllByShopId(shopId);

        ShopMenu menu = getMenuList.stream()
                .filter(m -> m.getId().equals(menuId))
                .findFirst()
                .orElseThrow(()->new ShopMenuException(ShopMenuErrorCode.SHOP_MENU_ERROR_CODE));

        menu.changeShopMenuStatus(shopMenuStatus);

        Menu changedMenu = menu.getMenu();

        return changedMenu;

    }

}
