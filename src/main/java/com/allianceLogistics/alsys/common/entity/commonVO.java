package com.allianceLogistics.alsys.common.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class commonVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private String	errorNo;
	private String	errorMsg;
	private String	rtnYn;	//리턴값 Y 성공, N실패
	private String	rtnMsg;	//리턴 메세지
	private	String	saveUser;	
	private	String	saveTime;
	private	String	updtUser;
	private	String	updtTime;
	
	private	String	commCd;		//마스터 공통 코드
	private	String	commNm;		//마스터 공통 명칭
	private	String	commDesc;	//마스터 공통 상세설명
	private	String	comdCd;		//상세 공통 코드
	private	String	comdNm;		//상세 공통 명칭
	private	String	comdDesc;	//상세 공통 상세설명
	private	String	tblCommMId;			//공통코드 고유아이디
	
	private	String	cmpyCd				;	//회사코드
	private	String	cmpyNm				;	//회사명칭	
	
	private	String	useYn				;	//사용여부
	
	private	String	windowId			;	//팝업창 아이디
	private	String	saveGubn			;	//저장인지 수정인지 구별
	private	String	rowId			;	//로우아이디
	
	private	String	fileName	;
	private	String	saveUserNm	;	//저장자이름
	private	String	cud;

	private	String	jsessionId;

	private	String	loginTime;
	
	//20220517 정연호 kpp api를 통해 끌어온 오더인지 아닌지를 구별
	private	String	outCmpyCd;

}