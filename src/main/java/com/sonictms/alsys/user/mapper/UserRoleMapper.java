package com.sonictms.alsys.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.sonictms.alsys.user.entity.UserRole;

@Component
@Mapper
public interface UserRoleMapper {

    void setUserRoleInfo(@Param("param") UserRole param);

}