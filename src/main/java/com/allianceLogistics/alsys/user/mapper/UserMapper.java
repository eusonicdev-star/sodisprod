package com.allianceLogistics.alsys.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.user.entity.Repw;
import com.allianceLogistics.alsys.user.entity.User;

@Component
@Mapper

public interface UserMapper {
	//스프링 부트 시큐리티 아이디로 인증정보 불러오기
    User loadUserByUsername(String username);

    //로그인 한 후 아이디로 정보찾기
    User findUserInfoByLoginId(User param);

    //로그인했을때 인증 정보 넣기
    User loginHistorySave(User user);

	//비밀번호 변경을 위한 인증번호 요청하기
    Repw repwReqSmsByPhoneNo(Repw param);
	
    //인증번호를 넣고 인증하기
    Repw repwSendReqNo(Repw param);
    
    //비밀번호변경하는 작업 수행
    Repw changePwSend(Repw param);

    //사용자 등록하기
    int setUserInfo(@Param("param") User param);
}