package com.sonictms.alsys.erp.erp991004.entity;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp991004VO extends CommonVO {
		
	private static final long serialVersionUID = 1L;
	
	private	String	userId			;		//사용자아이디
	private	String	userNm			;		//사용자이름
	private	String	tblUserMId		;		//////사용자 테이블 고유ID
	private	String	cnntSysCd		;
	private	String	cnntTime		;
	private	String	cnntIp			;
	
	
	
}