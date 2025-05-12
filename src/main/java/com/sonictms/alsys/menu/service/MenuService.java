package com.sonictms.alsys.menu.service;

import com.sonictms.alsys.menu.entity.MenuVO;
import com.sonictms.alsys.menu.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuMapper menuMapper;

    public List<MenuVO> menuList(MenuVO menuVO) {
        return menuMapper.menuList(menuVO);
    }
}