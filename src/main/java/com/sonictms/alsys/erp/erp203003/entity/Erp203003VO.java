package com.sonictms.alsys.erp.erp203003.entity;

import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class Erp203003VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private	String	dlvyCnfmDt		;
	private	String	soNo            ;
	private	String	agntCd          ;
	private	String	agntNm          ;
	private	String	dcCd            ;
	private	String	dcNm            ;
	private	String	tblUserMId      ;
	private	String	userNm          ;
	private	String	evalItem1       ;
	private	String	evalItem2       ;
	private	String	evalItem3       ;
	private	String	evalSpcl1       ;
	private	String	evalSpcl2       ;
	private	String	evalSpcl3       ;
	private	String	evalMsg         ;
	
	private	String	ordrCnt			;
	private	String	rspdCnt			;
	private	String	rspdRate		;
	
	
	//검색조건
	private	String	dtType			;	//날짜유형 3 : 배송완료일
	private	String	fromDt			;	
	private	String	toDt			;	
	
	private	String	agntList		;	//화주사코드
	private	String	dcList			;	//물류센터코드
	private	String	instErList		;	//배송기사코드
	private	String	soNoList		;	//AL오더번호
}