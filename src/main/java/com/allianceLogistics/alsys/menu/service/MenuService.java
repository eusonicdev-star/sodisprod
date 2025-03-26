package com.allianceLogistics.alsys.menu.service;

import com.allianceLogistics.alsys.menu.entity.MenuVO;
import com.allianceLogistics.alsys.menu.mapper.MenuMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuMapper menuMapper;

    public List<MenuVO> menuList(MenuVO menuVO) {
        return menuMapper.menuList(menuVO);
    }
}