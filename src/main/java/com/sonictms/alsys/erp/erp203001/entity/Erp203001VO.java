package com.sonictms.alsys.erp.erp203001.entity;

import com.sonictms.alsys.common.entity.commonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp203001VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private	String	fromDt				;		//검색시작일
	private	String	toDt				;		//검색종료일
	private	String	instCtgrList		;		//시공카테고리 리스트
	private	String	instTypeList		;		//시공유형 리스트
	private	String	instSeatTypeList  	;		//시공좌석분류 리스트
	private	String	instZoneTypeList	;		//권역유형리스트
	private	String	instZoneCdList		;		//권역리스트
		
	private	String	instPlanId		;		//시공계획 고유ID
	private	String	instPlanYn		;		//시공계획 여부
	private	String	tblSoMId		;		//판매오더 고유ID
	private	String	agntCd			;		//화주사코드
	private	String	agntNm			;		//화주사명칭
	private	String	soNo			;		//판매오더번호
	private	String	soType			;		//판매오더유형코드
	private	String	soTypeNm		;		//판매오더유형명칭
	private	String	soStatCd		;		//판매오더상태코드
	private	String	soStatNm		;		//판매오더상태명칭
	private	String	mallCd			;		//쇼핑몰
	private	String	acptEr			;		//고객명
	private	String	postCd			;		//우편번호
	private	String	addr1			;		//주소1
	private	String	addr2			;		//주소2
	private	String	dlvyRqstMsg		;		//배송요청메세지
	private	String	custSpclTxt		;		//고객특별요청
	private	String	instYn			;		//시공여부
	private	String	instCtgr		;		//시공카테고리코드
	private	String	instCtgrNm		;		//시공카테고리명칭
	private	String	instType		;		//시공유형코드
	private	String	instTypeNm		;		//시공유형명칭
	private	String	instSeatType	;		//시공좌석유형코드
	private	String	instSeatTypeNm	;		//시공좌석유형명칭
	private	String	zoneType		;		//권역유형코드
	private	String	zoneTypeNm		;		//권역유형명칭
	private	String	zoneCd			;		//권역코드
	private	String	zoneNm			;		//권역명칭
	private	String	dcCd			;		//물류센터코드
	private	String	dcNm			;		//물류센터명칭
	private	String	ndlvyYn			;		//ndlvyYn (모르겠다)
	private	String	qty				;		//수량
	private	String	cbm				;		//CBM
	private	String	instCost		;		//시공비
	private	String	instStatCd		;		//시공상태코드
	private	String	paltNoxx		;		//팔렛트번호
	private	String	seq				;		//순번
	private	String	instCmpy		;		//시공사코드
	private	String	instEr			;		//시공기사
	private	String	instPlanDt		;		//시공계획시간
	private	String	cnfmTime		;		//
	
	private	String	userNm			;		//이름찾기용
	private	String	userId			;		//이름찾기용
	
	private	String	totCnt			;		//총건수
	private	String	xtotCnt			;		//미확정건수
	
	private	String	tblSoPId		;		//
	private	String	tblSoDId		;		//
	
	private	String	rqstDt			;		//요청일(?)
	private	String	instDt			;		//시공일
	private	String	dlvyCost		;		//배송비(?)
	
	private	String	tblUserMId;	////사용자 USER_ID에 대한 테이블 고유 아이디
	
	private	String	prodCd;
	private	String	prodNm;
	private	String	smplAddr;
	private	String	planYn;	//시공계획 유뮤
	private	String	restYn;	//휴가우무
	
	private	String	hldyYn;	//휴가유무
	private	String	mobileTranYn;	//모바일 전송 여부
	
	//20211231 정연호. 품목 리스트 추가
	private String prodList;
	private String soList;
}