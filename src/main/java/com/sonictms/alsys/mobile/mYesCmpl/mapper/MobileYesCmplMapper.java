package com.sonictms.alsys.mobile.mYesCmpl.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.sonictms.alsys.mobile.mYesCmpl.entity.MobileYesCmplVO;

@Component
@Mapper
public interface MobileYesCmplMapper {

	//모바일 배송완료 onCreate 조회
	MobileYesCmplVO mYesCmplOnCreate(MobileYesCmplVO mobileYesCmplVO);
	
	//모바일 배송완료 처리 저장 SAVE
	MobileYesCmplVO mYesCmplSaveStat(MobileYesCmplVO mobileYesCmplVO);
	
	//모바일 배송완료 취소 처리 저장 DEL
	MobileYesCmplVO mYesCmplDel(MobileYesCmplVO mobileYesCmplVO);

	//모바일 배송완료 알림톡 결과 저장
	MobileYesCmplVO mYesCmplSaveTalk(MobileYesCmplVO mobileYesCmplVO);
	
}