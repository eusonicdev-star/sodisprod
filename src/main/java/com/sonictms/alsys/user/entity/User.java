package com.sonictms.alsys.user.entity;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
//@ToString
@ToString(callSuper=true,exclude = {"password","passwordConfirm"})

public class User extends CommonVO {

	private static final long serialVersionUID = 1L;

	private int 	id				;	//사용자 저장테이블의 고유아이디
	private String	password		;	//암호
	private String	passwordConfirm	;	//암호확인
	private String	loginId			;	//사용자아이디
	private String	userName		;	//사용자이름
	private int 	active			;	//사용가능/불가능
	private String	role			;	//권한역할

	private	String	userGrntCd		;	//사용자 그룹코드
	private	String	userGrntNm		;	//사용자 그룹코드

	private	String	userId			;	//시용자ID
	private	String	cnntSysCd		;	//접속한 시스템 ERP / MOBILE
	private	String	cnntIp			;	//접속한 아이피

	private	String	mobileGrntCd		;	//모바일 권한코드
	private	String	mobileGrntNm		;	//모바일 권한명칭

	private	String	userGrdCd		;	//사용자구분
	private	String	userGrdNm		;	
	
	private	String	agntCd		;	//소속화주
	private	String	agntNm		;	
	
	private	String	dcCd		;	//소속지점
	private	String	dcNm		;	

	private	String	tblUserMId	;	//20220303 정연호 추가
}