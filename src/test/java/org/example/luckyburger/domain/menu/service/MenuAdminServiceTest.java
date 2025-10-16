package org.example.luckyburger.domain.menu.service;

import org.example.luckyburger.domain.menu.dto.request.MenuCreateRequest;
import org.example.luckyburger.domain.menu.dto.request.MenuUpdateRequest;
import org.example.luckyburger.domain.menu.dto.response.MenuCreateResponse;
import org.example.luckyburger.domain.menu.dto.response.MenuUpdateResponse;
import org.example.luckyburger.domain.menu.entity.Menu;
import org.example.luckyburger.domain.menu.enums.MenuCategory;
import org.example.luckyburger.domain.menu.exception.NotFoundMenuException;
import org.example.luckyburger.domain.menu.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class MenuAdminServiceTest {

    private static final String menuName = "햄버거";
    private static final MenuCategory category = MenuCategory.HAMBURGER;
    private static final long price = 8000;

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private MenuAdminService menuAdminService;

    @Test
    void 메뉴_생성에_성공한다() {
        // given
        MenuCreateRequest request = new MenuCreateRequest(menuName, category, price);

        given(menuRepository.save(any(Menu.class))).willAnswer(i -> i.getArgument(0));

        // when
        MenuCreateResponse response = menuAdminService.createMenu(request);

        // then
        assertNotNull(response);
        assertEquals(menuName, response.name());
        assertEquals(category, response.menuCategory());
        assertEquals(price, response.price());
    }

    @Test
    void 메뉴_수정에_성공한다() {
        // given
        Long menuId = 1L;
        Menu existingMenu = Menu.of("기존버거", MenuCategory.HAMBURGER, 7000);
        MenuUpdateRequest request = new MenuUpdateRequest(menuName, category, price);

        given(menuRepository.findById(menuId)).willReturn(java.util.Optional.of(existingMenu));

        // when
        MenuUpdateResponse response = menuAdminService.updateMenu(menuId, request);

        // then
        assertNotNull(response);
        assertEquals(menuName, response.name());
        assertEquals(category, response.menuCategory());
        assertEquals(price, response.price());
    }

    @Test
    void 메뉴가_존재하지_않아_수정에_실패한다() {
        // given
        Long menuId = 1L;
        MenuUpdateRequest updateRequest = new MenuUpdateRequest(menuName, category, price);

        given(menuRepository.findById(menuId)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(NotFoundMenuException.class, () -> menuAdminService.updateMenu(menuId, updateRequest));
    }

    @Test
    void 메뉴_삭제에_성공한다() {
        // given
        Long menuId = 1L;
        Menu menu = Menu.of(menuName, category, price);
        given(menuRepository.findById(menuId)).willReturn(java.util.Optional.of(menu));

        // when
        menuAdminService.deleteMenu(menuId);

        // then
        verify(menuRepository).delete(menu);
    }

    @Test
    void 메뉴가_존재하지_않아_삭제에_실패한다() {
        // given
        Long menuId = 1L;
        given(menuRepository.findById(menuId)).willReturn(java.util.Optional.empty());

        // when & then
        assertThrows(NotFoundMenuException.class, () -> menuAdminService.deleteMenu(menuId));
    }
}
