package com.allianceLogistics.alsys.erp.erp202001.entity;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp202001VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private	String	dtType			;		//시공일 검색 조건 1. 기간. 2 범위
	private	String	fromDt			;		//시공일 검색 시작일
	private	String	toDt			;		//시공일 검색 종료일
	private	String	dtList			;		//시공일 범위
	private	String	instTypeList	;		//시공유형
	private	String	instCtgrList	;		//시공카테고리
	private	String	instZoneType	;		//권역유형
	private	String	instZoneList	;		//권역
	private	String	weekList		;		//요일

	private	String	capaId			;		//좌석 고유아이디

	private	String	instDt			;		//시공일자
	private	String	weekCd			;		//요일코드
	private	String	weekNm			;		//요일명칭
	private	String	zoneType		;		//권역유형코드
	private	String	zoneTypeNm		;		//권역유형명칭
	private	String	zoneCd			;		//권역코드
	private	String	zoneCdNm		;		//권역명칭
	private	String	capaType		;		//좌석유형
	private	String	capaTypeNm		;		//좌석유형명칭
	private	String	capaCtgr		;		//좌석카테고리
	private	String	capaCtgrNm		;		//좌석카테고리명칭
	private	String	totCapa			;		//전체좌석
	private	String	useCapa			;		//사용좌석
	private	String	remnCapa		;		//남은좌석
	private	String	capaPerDay		;		//1일좌석사용량

	private	String	instCtgr		;		//입력 시공카테고리 코드
	private	String	instType		;		//입력 시공유형 코드
	private	String	seatTypeList	;		//왼쪽 그리드 시공권역명 체크한것 리스트

	private	String	capaCnt			;		//생성좌석수
	
	private	String	seatType		;
	
	
	private	String	seatTypeNm	;	//시공좌석이름

	private	String	ordrNo		;	//오더번호

	private	String	agntCd		;	//화주사코드
	private	String	agntNm		;	//화주사명칭
	private	String	soType		;	//주문종류
	private	String	soTypeNm	;	//주문종류명칭
	private	String	soStatCd	;	//주문상태코드
	private	String	soStatNm	;	//주문상태명칭
	private	String	mallCd		;	//쇼핑몰
	private	String	acptEr		;	

	
	
	

}