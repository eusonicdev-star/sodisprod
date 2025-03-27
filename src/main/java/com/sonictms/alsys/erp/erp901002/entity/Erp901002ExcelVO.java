package com.sonictms.alsys.erp.erp901002.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901002ExcelVO implements Serializable {
		
	private static final long serialVersionUID = 1L;
	
	private	String	cmpyCd			;		
	private	String	agntCd			;
	private	String	mtrlType		;
	private	String	mtrlCd			;
	private	String	mtrlNm			;
	private	String	mtrlDesc		;
	private	String	statCd			;
	private	String	smplCd			;
	private	String	custPrc			;
	private	String	salePrc			;
	private	String	factPrc			;
	private	String	purcPrc			;
	private	String	sizeNm			;
	private	String	lnth			;
	private	String	dpth			;
	private	String	hght			;
	private	String	hght2			;
	private	String	cbm				;
	private	String	wght			;
	private	String	mtrlCtgr1		;
	private	String	mtrlCtgr2		;
	private	String	mtrlCtgr3		;
	private	String	setYn			;
	private	String	bomYn			;
	private	String	mtoYn			;
	private	String	stdYn			;
	private	String	instYn			;
	private	String	instCtgr		;
	private	String	instType		;
	private	String	instSeatType	;
	private	String	instCost		;
	private	String	instUrl			;
	private	String	instImgPath		;
	private	String	pstnNm			;
	private	String	dlvyYn			;
	private	String	dlvyType		;
	private	String	dlvyCost		;
	private	String	ndlvyYn			;
	private	String	thrDlvyYn		;
	private	String	dlvyLt			;
	private	String	cnvtMtrlCd		;
	private	String	cnvtMtrlNm		;
	private	String	cnvtQty			;
	private	String	cnvtUnit		;
	private	String	memo			;
	private	String	useYn			;
	private	String	zoneType		;
	private	String	pickZone		;
	private	String	saveUser		;
	private	String	rtnYn			;
	private	String	rtnMsg			;
	private	String	exclRow			;
	private	String	inptSys			;
}