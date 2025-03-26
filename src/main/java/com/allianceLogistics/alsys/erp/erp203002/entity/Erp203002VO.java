package com.allianceLogistics.alsys.erp.erp203002.entity;



import java.util.ArrayList;
import java.util.List;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp203002VO extends commonVO{
		
	private static final long serialVersionUID = 1L;
//	private	List<Erp203002VO>	detailData;

	private	String	instPlanId		;	//시공계획ID
	private	String	tblSoMId		;	//주문헤더ID
	private	String	tblSoPId		;	//주문상세ID
	private	String	tblSoDId		;	//주문부품ID
	private	String	mobileTransYn	;	//모바일<br>전송유무
	private	String	mobileTransTime	;	//모바일<br>전송시간
	
	private	String	instDt			;	//시공일
	private	String	rqstDt			;	//요청일
	private	String	dcCd			;	//물류센터코드
	private	String	dcNm			;	//물류센터명칭
	private	String	paltNoxx		;	//팔렛트명칭
	private	String	seq				;	//순번
	private	String	instCmpy		;	//시공사
	private	String	tblUserMId		;	//시공기사고유ID
	private	String	instEr			;	//시공기사이름
	private	String	instStatCd		;	//계획상태코드

	private	String	instStatNm		;	//계획상태명칭
	private	String	instPlanDt		;	//계획일
	private	String	cnfmTime		;	//확정일
	private	String	agntCd			;	//화주사코드
	private	String	agntNm			;	//화주사명칭
	private	String	soNo			;	//오더번호
	private	String	instCtgr		;	//시공카테고리코드
	private	String	instCtgrNm		;	//시공카테고리명칭
	private	String	instType		;	//시공유형코드
	private	String	instTypeNm		;	//시공유형명칭
	private	String	seatType		;	//시공좌석유형코드
	private	String	instSeatTypeNm	;	//시공좌석유형명칭
	private	String	zoneType		;	//권역유형코드
	private	String	zoneTypeNm		;	//권역유형명칭
	private	String	zoneCd			;	//권역코드
	private	String	zoneNm			;	//권역명칭
	private	String	cbm				;	//CBM
	private	String	dlvyCostType	;	//운임구분코드
	private	String	dlvyCostTypeNm	;	//운임구분명칭
	private	String	instCost		;	//시공비
	private	String	dlvyCost		;	//배송비
	private	String	dlvyRqstMsg		;	//배송메세지
	private	String	custSpclTxt		;	//고객특이상항
	
	private	String	agntList	;		//화주코드리스트
	private	String	dcList		;		//물류센터코드 리스트
	private	String	paltNoxxList;		//팔렛트번호 리스트
	private	String	tblUserMList;		//시공기사고유ID 리스트
	private	String	soList		;		//오더번호리스트
	private	String	instPlanStat;		//시공계획상태코드
	
	private	String	valIdChkYn	;	//삭제시 체크 변수

}