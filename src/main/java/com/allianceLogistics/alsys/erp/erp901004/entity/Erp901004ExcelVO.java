package com.allianceLogistics.alsys.erp.erp901004.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901004ExcelVO implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	private	String	cmpyCd	;
	private	String	agntCd	;
	private	String	mtrlCd	;
	private	String	seq		;
	private	String	cmtrlCd	;
	private	String	qty		;
	private	String	unit	;
	private	String	useYn	;

	private	String	saveUser		;
	private	String	rtnYn			;
	private	String	rtnMsg			;
	private	String	exclRow			;
	private	String	inptSys			;
}