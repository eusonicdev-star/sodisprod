package com.allianceLogistics.alsys.config;

//spring boot logout handler

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TaskImplementingLogoutHandler implements LogoutHandler {		//LogoutHandler 를 빌려다가 TaskImplementingLogoutHandler 를 만듦
  
    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res,
            Authentication authentication) {		//logout 함수는 LogoutHandler 안에 들어있다

      log.info("로그아웃 되었습니다.");
    }
}