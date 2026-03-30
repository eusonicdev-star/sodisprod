package com.sonictms.alsys.erp.erp991003.entity;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true,exclude = {"userPw"})
public class Erp991003VO extends CommonVO {
		
	private static final long serialVersionUID = 1L;
	
	private	String	userId			;		//사용자아이디
	private	String	userNm			;		//사용자이름
	private	String	dcCd			;		//소속지점코드
	private	String	dcNm			;		//소속지점명
	private	String	telNo			;		//일반전화번호
	private	String	hp				;		//휴대전화번호
	private	String	userGrntCd		;		//시스템권한코드
	private	String	userGrntNm		;		//시스템권한명칭
	private	String	srchUser		;		//사용자 검색
	
	private	String	tblUserMId		;		//사용자 테이블 고유ID
	private	String	saveGubn		;		//입력인지 수정인지 구분

	private	String	deptCd			;		//사용자부서코드
	private	String	deptNm			;		//사용자부서명칭
	private	String	emplNo			;
	private	String	email			;
	private	String	userGrd			;
	private	String	agntCd			;
	private	String	agntNm			;
	private	String	inptDt			;		//입사일
	private	String	outDt			;
	private	String	userKey			;
	private	String	memo			;
	private	String	userPw;
	private	String	team;
	
	
	

	private	String	userGrdNm		;

	private	String	teamNm			;

	private	String	agntCdNm		;


	private	String	mobileGrntCd	;
	private	String	mobileGrntNm	;
	private	String	agntDeptCd		;
	private	String	agntDeptNm		;


	
	
}