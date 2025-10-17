package org.example.luckyburger.domain.shop.service;

import lombok.AllArgsConstructor;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.shop.entity.ShopMenu;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.exception.shop.ShopErrorCode;
import org.example.luckyburger.domain.shop.exception.shop.ShopException;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ShopMenuService {

    private final ShopMenuRepository shopMenuRepository;

    public List<Menu> getShopMenuList(Long shopId){

        List<ShopMenu> getMenuList = shopMenuRepository.findAllByShopId(shopId);

        return getMenuList.stream()
                .map(ShopMenu::getMenu)
                .collect(Collectors.toList());
    }

    //shopId 필요??
    public Menu getMenuDetail(Long shopId, Long menuId){

        ShopMenu menuDetail = shopMenuRepository.findByMenuId(menuId);

        Menu menu = menuDetail.getMenu();

        return menu;
    }

    //가게에 해당하는 메뉴를 변경
    public Menu updateMenuStatus(Long shopId, Long menuId,ShopMenuStatus shopMenuStatus){

        List<ShopMenu> getMenuList = shopMenuRepository.findAllByShopId(shopId);

        ShopMenu menu = getMenuList.stream()
                .filter(m -> m.getId().equals(menuId))
                .findFirst()
                .orElse(null);

        assert menu != null;
        menu.changeShopMenuStatus(shopMenuStatus);

        Menu changedMenu = menu.getMenu();

        return changedMenu;

    }

}
