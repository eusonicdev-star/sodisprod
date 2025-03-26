package com.allianceLogistics.alsys.mobile.mNoCmpl.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.mobile.mNoCmpl.entity.MobileNoCmplVO;


@Component
@Mapper
public interface MobileNoCmplMapper {

	
	
	//모바일 미마감 onCreate 조회
	MobileNoCmplVO mNoCmplOnCreate(MobileNoCmplVO mobileNoCmplVO);
	
	//모바일 미마감 처리 결과 저장 SAVE
	MobileNoCmplVO mNoCmplSaveStat(MobileNoCmplVO mobileNoCmplVO);
	
	//모바일 미마감 취소 처리 저장 DEL
	MobileNoCmplVO mNoCmplDel(MobileNoCmplVO mobileNoCmplVO);
		
	//모바일 미마감 알림톡 결과 저장 SAVE
	MobileNoCmplVO mNoCmplSaveTalk(MobileNoCmplVO mobileNoCmplVO);
		
	
	
	/******************************
	
	
	//모바일 배송 리스트 순번변경하기
	MobileNoCmplVO mNoCmplSeqUpdate(MobileNoCmplVO MobileNoCmplVO);
	//모바일 배송 리스트 통화 카운트 증가
	MobileNoCmplVO mNoCmplTelUpdate(MobileNoCmplVO MobileNoCmplVO);

	//모바일 배송 상세 조회하기
	List<MobileNoCmplVO> mNoCmplDetailSrch(MobileNoCmplVO MobileNoCmplVO);
				

	//모바일 배송 상차 완료 하기
	MobileNoCmplVO mNoCmplLiftSave(MobileNoCmplVO MobileNoCmplVO);
	
	

	//모바일 배송 상차 취소 하기
	MobileNoCmplVO mNoCmplLiftCancel(MobileNoCmplVO MobileNoCmplVO);
	
	********************************/

}