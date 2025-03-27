package com.sonictms.alsys.erp.erp101002.entity;



import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp101002VO extends commonVO{
		
	private static final long serialVersionUID = 1L;


	private	String	agntCd		;	//화주사코드
	private	String	exclTmplType	;	//양식구분코드
	private	String	agntNm		;	//화주사명칭
	private	String	tmplId			;	//양식고유ID
	private	String	exclTmplName	;	//양식구분명칭
	private	String	a				;	
	private	String	b				;
	private	String	c				;
	private	String	d				;
	private	String	e				;
	private	String	f				;
	private	String	g				;
	private	String	h				;
	private	String	i				;
	private	String	j				;
	private	String	k				;
	private	String	l				;
	private	String	m				;
	private	String	n				;
	private	String	o				;
	private	String	p				;
	private	String	q				;
	private	String	r				;
	private	String	s				;
	private	String	t				;
	private	String	u				;
	private	String	v				;
	private	String	w				;
	private	String	x				;
	private	String	y				;
	private	String	z				;
	
	private	String	srchCode		;	//화주사코드 검색
	private	String	srchName		;	//화주사명칭 검색
	//private	String[]	updtData	;	//양식수정팝업 데이터 전달용
	//private	List<Erp101002VO>	updtData;
	
	private	String	seq				;		//그리드헤더 순번
	private	String	tmpType			;		//그리드헤더 유형
	
	
	
	//엑셀저장
	private	String	exclRow		;
	private	String	cmpyCd		;
	private	String	refSoNo		;
	private	String	orgnSoNo	;
	private	String	soType		;
	private	String	soStatCd	;
	private	String	mallCd		;
	private	String	brand		;
	private	String	ordrInptDt	;
	private	String	rcptDt		;
	private	String	rqstDt		;
	private	String	dlvyCnfmDt	;
	private	String	acptEr		;
	private	String	ordrEr		;
	private	String	acptTel1	;
	private	String	acptTel2	;
	private	String	postCd		;
	private	String	addr1		;
	private	String	addr2		;
	private	String	dlvyRqstMsg	;
	private	String	custSpclTxt	;
	private	String	costType	;
	private	String	rcptCost	;
	private	String	passCost	;
	private	String	prodCd		;
	private	String	prodNm		;
	private	String	qty			;
	private	String	dcCd		;
	private	String	memo		;
	private	String	inptSys		;

	private	String	comboCd;
	private	String	comboNm;
	
	
}