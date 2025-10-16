package org.example.luckyburger.domain.menu.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.luckyburger.domain.menu.dto.response.MenuGetResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.exception.NotFoundMenuException;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Getter
@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class MenuService {

    private final MenuRepository menuRepository;

    // 메뉴 전체 조회
    @Transactional(readOnly = true)
    public Page<MenuGetResponse> getAllMenuResponse(Pageable pageable) {
        Page<Menu> menus = menuRepository.findAll(pageable);

        return menus.map(MenuGetResponse::from);
    }

    // 메뉴 단일 조회
    @Transactional(readOnly = true)
    public MenuGetResponse getMenuResponse(Long menuId) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(NotFoundMenuException::new);

        return MenuGetResponse.from(menu);
    }
}
