package com.allianceLogistics.alsys.erp.erp902005.entity;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp902005VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

		private	String	clndType		;		//달력유형
		private	String	clndYear		;		//년도
		private	String	dateFrom		;
		private	String	dateTo			;
		private	String	weekList		;
		
		private	String	tblCalendaM		;	//달력테이블 고유Id
		private	String	clndTypeNm		;
		private	String	clndDay			;
		private	String	clndDaySeq		;
		private	String	clndWeekCd		;
		private	String	clndWeekNm		;
		private	String	clndWeekSeq		;
		private	String	dlvyWorkYn		;
		private	String	cdcWorkYn		;
		private	String	rdcWorkYn		;
		private	String	memo			;
		
		
	
	
}