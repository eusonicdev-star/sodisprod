package com.sonictms.alsys.mobile.mLogin.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sonictms.alsys.mobile.mLogin.entity.MobileLoginVO;
import com.sonictms.alsys.mobile.mLogin.mapper.MobileLoginMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class MobileLoginService  {
	 
	@Autowired
	private  MobileLoginMapper mobileLoginMapper;
	

	
	//아이디 가지고 아이디,암호 정보찾기
	public MobileLoginVO mLogin(MobileLoginVO mobileLoginVO) { 
		mobileLoginVO = mobileLoginMapper.mLogin(mobileLoginVO);
		return mobileLoginVO; 
	}
	
	//아이디 가지고 사용자 정보 찾기
	public MobileLoginVO mUserInfo(MobileLoginVO mobileLoginVO) { 
		mobileLoginVO = mobileLoginMapper.mUserInfo(mobileLoginVO);
		return mobileLoginVO; 
	}
	
	
	//로그인 되었으면 히스토리를 저장한다
	public MobileLoginVO mUserHist(MobileLoginVO mobileLoginVO) { 
		mobileLoginVO = mobileLoginMapper.mUserHist(mobileLoginVO);
		return mobileLoginVO; 
	}

	
} 