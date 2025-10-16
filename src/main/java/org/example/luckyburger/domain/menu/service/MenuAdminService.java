package org.example.luckyburger.domain.menu.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
import org.example.luckyburger.domain.menu.dto.request.MenuUpdateRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuAdminService {

    private final MenuRepository menuRepository;
    private final MenuEntityFinder menuEntityFinder;

    @Transactional
    public MenuResponse createMenu(MenuCreateRequest request) {

        Menu menu = Menu.of(request.name(), request.menuCategory(), request.price());

        Menu savedMenu = menuRepository.save(menu);

        return MenuResponse.from(savedMenu);
    }

    @Transactional
    public MenuResponse updateMenu(Long menuId, MenuUpdateRequest request) {

        Menu menu = menuEntityFinder.getMenu(menuId);

        menu.updateMenu(request.name(), request.menuCategory(), request.price());

        return MenuResponse.from(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuEntityFinder.getMenu(menuId);

        menuRepository.delete(menu);
    }
}
