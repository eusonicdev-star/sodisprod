package com.sonictms.alsys.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TaskImplementingLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest req, HttpServletResponse res, Authentication authentication) {

    }
}