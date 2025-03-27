package com.sonictms.alsys.user.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.sonictms.alsys.user.entity.Role;

@Component
@Mapper
public interface RoleMapper {
    Role getRoleInfo(@Param("role") String role);
}