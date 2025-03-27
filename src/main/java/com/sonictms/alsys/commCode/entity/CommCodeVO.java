package com.sonictms.alsys.commCode.entity;



import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class CommCodeVO extends commonVO{
	
	private static final long serialVersionUID = 1L;
	

	private String	comboType;		//가져올 공통코드 유형

	
	private	String	comboCd;		//콤보코드
	private	String	comboNm;		//콤보명칭
	
	private	String	srchCtgr;		//공통 검색종류
	private	String	srchType;		//검색조건 1) 명칭검색 2)코드검색 3)멀티검색
	private	String	srchNm;			//1명칭검색
	private	String	srchCd;			//2코드검색
	private	String	srchList;		//멀티검색
	private	String	srchInput;		///검색하는 종류가 무엇인지구분 INPUT의 type를 구분
	
	private	String	srchArea	;
	private	String	agntCd		;		//화주사코드
	private	String	agntNm		;		//화주사명칭

}