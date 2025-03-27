package com.sonictms.alsys.menu.mapper;

import com.sonictms.alsys.menu.entity.MenuVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {

	List<MenuVO> menuList( MenuVO menuVO);
	
}