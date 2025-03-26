package com.allianceLogistics.alsys.user.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.allianceLogistics.alsys.user.domain.UserPrincipal;
import com.allianceLogistics.alsys.user.entity.Repw;
import com.allianceLogistics.alsys.user.entity.Role;
import com.allianceLogistics.alsys.user.entity.User;
import com.allianceLogistics.alsys.user.entity.UserRole;
import com.allianceLogistics.alsys.user.mapper.RoleMapper;
import com.allianceLogistics.alsys.user.mapper.UserMapper;
import com.allianceLogistics.alsys.user.mapper.UserRoleMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserMapper userMapper;

	@Autowired
    private RoleMapper roleMapper;

	@Autowired
	private UserRoleMapper userRoleMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //로그인 후 아이디로 정보찾기
	public User findUserInfoByLoginId(User user) {
		return userMapper.findUserInfoByLoginId(user);
	}

	//비밀번호 변경을 위한 인증번호 요청하기
	public Repw repwReqSmsByPhoneNo(Repw param) {
		userMapper.repwReqSmsByPhoneNo(param);
		return param;
	}
	
	//인증번호를 넣고 인증하기
	public Repw repwSendReqNo(Repw param) {
		userMapper.repwSendReqNo(param);
		return param;
	}

	//비밀번호변경하는 작업 수행
	public Repw changePwSend(Repw param) {
		param.setPassword(bCryptPasswordEncoder.encode(param.getPassword()));	//암호를 암호화 함
		userMapper.changePwSend(param);
		return param;
	}

	//로그인했을때 인증 정보 넣기(히스토리 넣기)
	public User	loginHistorySave(User user) {
		userMapper.loginHistorySave(user);
		return user;
	}

	//사용자 등록하기
	public void saveUser(User user) {
		log.info("");
		log.info("");
		log.info("********************");
		log.info("********************");
		log.info("********************");
		log.info("**사용여부확인용도**");
		log.info("********************");
		log.info("********************");
		log.info("");
		log.info("");
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        userMapper.setUserInfo(user);

        Role role = roleMapper.getRoleInfo("ADMIN");

		UserRole userRole = new UserRole();
		userRole.setRoleId(role.getId());
		userRole.setUserId(user.getId());

		userRoleMapper.setUserRoleInfo(userRole);
	}

	//스프링 부트 [아이디로 인증 정보 호출]
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userMapper.loadUserByUsername(username);
		return new UserPrincipal(user);
	}
} 