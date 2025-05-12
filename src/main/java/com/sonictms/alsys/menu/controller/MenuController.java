package com.sonictms.alsys.menu.controller;

import com.sonictms.alsys.menu.entity.MenuVO;
import com.sonictms.alsys.menu.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MenuController {

    private final MenuService menuService;

    @GetMapping(value = {"menu"})
    public ModelAndView getMenu() {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("menu/menu");
        return modelAndView;
    }

    @RequestMapping(value = {"menuList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<MenuVO> menuList(MenuVO menuVO) {

        return menuService.menuList(menuVO);
    }
}