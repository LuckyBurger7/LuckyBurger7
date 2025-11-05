package org.example.luckyburger.domain.menu.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.common.consts.CacheConst;
import org.example.luckyburger.domain.menu.dto.request.MenuRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.example.luckyburger.domain.shop.enums.ShopMenuStatus;
import org.example.luckyburger.domain.shop.repository.ShopMenuRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuAdminService {

    private final MenuRepository menuRepository;
    private final MenuEntityFinder menuEntityFinder;
    private final ShopMenuRepository shopMenuRepository;

    @Transactional
    @CacheEvict(value = "shopCache", allEntries = true, beforeInvocation = true)
    public MenuResponse createMenu(MenuRequest request) {

        Menu menu = Menu.of(request.name(), request.menuCategory(), request.price());

        Menu savedMenu = menuRepository.save(menu);

        // 메뉴 초기 상태 DEACTIVATE
        shopMenuRepository.saveForAllShop(savedMenu.getId(), ShopMenuStatus.DEACTIVATE.name());

        return MenuResponse.from(savedMenu);
    }

    @Transactional
    @CacheEvict(value = CacheConst.MENU, key = "#menuId")
    public MenuResponse updateMenu(Long menuId, MenuRequest request) {

        Menu menu = menuEntityFinder.getMenuById(menuId);

        menu.updateMenu(request.name(), request.menuCategory(), request.price());

        return MenuResponse.from(menu);
    }

    @Transactional
    @CacheEvict(value = CacheConst.MENU, key = "#menuId")
    public void deleteMenu(Long menuId) {
        Menu menu = menuEntityFinder.getMenuById(menuId);

        menuRepository.delete(menu);
    }
}
