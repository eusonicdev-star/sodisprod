package com.sonictms.alsys.mobile.mLogin.entity;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//@ToString
//@ToString(callSuper=true)
@ToString(callSuper=true,exclude = {"userPw","password"})
public class MobileLoginVO extends CommonVO {
		
	private static final long serialVersionUID = 1L;

	private	String	Id;
	private	String	userId;
	private	String	userPw;
	private	String	loginId;
	private	String	password;
	
	private	String	userName	;
	
	private	String	userGrntCd	;	//사용자권한코드
	private	String	userGrntNm	;	//사용자권한명칭
	private	String	userGrdCd	;	//사용자 등급
	private	String	userGrdNm	;	//사용자 등급
	
	private	String	dcCd;
	private	String	dcNm;
    
    private String  cnntSysCd;
    private String  cnntIp;
    
    private	String	agntCd;
    
    private	String	agntNm;
    
    private	String	mobileGrntCd	;	//사용자권한코드
	private	String	mobileGrntNm	;	//사용자권한명칭
 
	//20220113 정연호. 사무실전화번호
	private	String	officeTel;
	
	//20220220 정연호 모바일 정보 기록
	private	String	mode	    ;
	private	String	versionCode	;
	private	String	versionName	;
	private	String	manufacturer;
	private	String	model	    ;
	private	String	os	        ;
	private	String	release	    ;
	
	//20220303 정연호 추가
	private	String	tblUserMId	;
}