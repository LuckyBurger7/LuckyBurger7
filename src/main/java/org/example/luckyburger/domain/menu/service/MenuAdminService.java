package org.example.luckyburger.domain.menu.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
import org.example.luckyburger.domain.menu.dto.request.MenuUpdateRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuCreateResponse;
import org.example.luckyburger.domain.menu.dto.response.MenuUpdateResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.exception.NotFoundMenuException;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuAdminService {

    private final MenuRepository menuRepository;

    @Transactional
    public MenuCreateResponse createMenu(MenuCreateRequest request) {

        Menu menu = Menu.of(request.name(), request.menuCategory(), request.price());

        menuRepository.save(menu);

        return MenuCreateResponse.from(menu);
    }

    @Transactional
    public MenuUpdateResponse updateMenu(Long menuId, MenuUpdateRequest request) {

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(NotFoundMenuException::new);

        menu.updateMenu(request.name(), request.menuCategory(), request.price());

        return MenuUpdateResponse.from(menu);
    }

    @Transactional
    public void deleteMenu(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(NotFoundMenuException::new);

        menuRepository.delete(menu);
    }
}
