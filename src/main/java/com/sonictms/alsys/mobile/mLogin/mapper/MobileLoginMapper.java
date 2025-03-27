package com.sonictms.alsys.mobile.mLogin.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.sonictms.alsys.mobile.mLogin.entity.MobileLoginVO;



@Component
@Mapper
public interface MobileLoginMapper {

	
	//아이디 가지고 아이디,암호 정보찾기
	MobileLoginVO mLogin( MobileLoginVO mLoginVO);
	
	//아이디 가지고 사람정보찾기
	MobileLoginVO mUserInfo( MobileLoginVO mLoginVO);
	
	//로그인 되었으면 히스토리를 저장한다
	MobileLoginVO mUserHist( MobileLoginVO mLoginVO);	
	
	/*
	//배송확정일 가능한 날짜 조회
	List<MobileLoginVO> mLoginp1List( MobileLoginVO mLoginVO);
	
	
	//시공좌석 일괄생성 팝업창에서 일괄생성 헤더 저장하기
	MobileLoginVO mLoginp1HeaderSave( MobileLoginVO mLoginVO);	
	//시공좌석 일괄생성 팝업창에서 일괄생성 상세 저장하기
	MobileLoginVO mLoginp1DetailSave( MobileLoginVO mLoginVO);	
		
	//주문수정 팝업에서 헤더 불러오기		//헤더는 1줄만 불러옴
	MobileLoginVO mobileLoginVOp3HeaderList( MobileLoginVO mobileLoginVOVO);
	
	
	//주문수정 팝업에서 상세 불러오기		//상세는 리스트
	List<MobileLoginVO> mobileLoginVOp3DetailList( MobileLoginVO mobileLoginVOVO);
		


	//시공좌석 일괄생성 팝업창에서 일괄생성 헤더 수정하기
	MobileLoginVO mobileLoginVOp1HeaderUpdt( MobileLoginVO mobileLoginVOVO);		
	
*/
		

}