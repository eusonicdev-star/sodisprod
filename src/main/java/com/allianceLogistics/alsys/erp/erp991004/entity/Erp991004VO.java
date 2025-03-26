package com.allianceLogistics.alsys.erp.erp991004.entity;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp991004VO extends commonVO{
		
	private static final long serialVersionUID = 1L;
	
	private	String	userId			;		//사용자아이디
	private	String	userNm			;		//사용자이름
	private	String	tblUserMId		;		//////사용자 테이블 고유ID
	private	String	cnntSysCd		;
	private	String	cnntTime		;
	private	String	cnntIp			;
	
	
	
}