package com.sonictms.alsys.erp.erp991002.entity;

import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp991002VO extends commonVO{
	
	
	private static final long serialVersionUID = 1L;
	

	private	String	userGrntCd			;	//사용자권한그룹코드
	private	String	userGrntNm			;	//사용자권한그룹명칭
	private	String	sysCd				;	//시스템코드
	private	String	sysNm				;	//시스템명칭.
	private	String	tblMenuGrntMId		;	//메뉴권한 고유아이디
	private	String	tblMenuMId			;	//메뉴 고유아이디
	
	private	String	menuNm1				;	//레벨1메뉴이름
	private	String	menuNm2				;	//레벨2메뉴이름
	private	String	menuNm3				;	//레벨3메뉴이름
	private	String	menuNm4				;	//레벨4메뉴이름
	
	private	String	menuCd				;	//메뉴코드
	private	String	menuLvl				;	//메뉴코드
	private	String	url					;	//메뉴코드
	
	private	String	chk					;	//체크여부 
	private	String	saveGubn			;	//저장유형 INS 입력, UPDT 수정

	
	
	
}