package com.allianceLogistics.alsys.erp.erp901006.entity;



import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901006VO extends commonVO{
		
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
	//private	List<Erp901006VO>	updtData;
}