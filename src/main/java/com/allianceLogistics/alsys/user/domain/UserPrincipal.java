package com.allianceLogistics.alsys.user.domain;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.allianceLogistics.alsys.user.entity.User;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Getter
@Slf4j
//여기에서 로그인처리를함
public class UserPrincipal implements UserDetails {
	
	
	private static final long serialVersionUID = 1L;
	
	private User user;	//user vo 정의

    public UserPrincipal(User user) {	//그아이디로 디비에서 찾고

    	//log.info("UserPrincipal user : " + user);
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {	//계정이 갖고있는 권한목록을 리턴

    	//log.info("UserPrincipal Collection");
        return Arrays.asList(new UserGrant());
    }

    @Override
    public String getPassword() {	//UserDetails 에 정의된 암호
        return user.getPassword();
    }

	/*
	 * @Override public String getUsername() { return user.getUserName(); }
	 */
    
    @Override
    public String getUsername() {	//UserDetails 에 정의된 아이디
    	
        return user.getLoginId();
    }   

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

    	boolean val = true;
    	if(user != null)
    	{
    		if(user.getUseYn().equals("Y"))
    		{
    			val= true;
    		}
    		else
    		{
    			val= false;
    		}
    	}
    	else
    	{
    		val= false;
    	}
		return val;
    }

    //추가사항
    public String getLoginId() {
        return user.getLoginId();
    }

    public String getLoginName() {
        return user.getUserName();
    }
    
    public String getRole() {
        return user.getRole();
    }
    
    public String getCmpyCd() {
        return user.getCmpyCd();
    }
}