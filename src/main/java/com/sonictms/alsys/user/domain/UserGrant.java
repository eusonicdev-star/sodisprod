package com.sonictms.alsys.user.domain;

import org.springframework.security.core.GrantedAuthority;

public class UserGrant implements GrantedAuthority {

	private static final long serialVersionUID = 1L;

	@Override
    public String getAuthority() {
        return "ADMIN";
    }
}