package com.allianceLogistics.alsys.erp.erp902006.entity;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp902006VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

		private	String	zoneType		;		//권역유형
		private	String	zoneTypeNm		;		//
		private	String	zoneCd			;		//권역
		private	String	zoneNm			;
		private	String	tblLgstWeekdlvyMId		;	//권역별-요일배송 맵핑 고유 아이디
		private	String	weekdlvyYn		;			//요일배송저ㅏㄱ용유무
		private	String	monYn			;
		private	String	tueYn			;
		private	String	wedYn			;
		private	String	thuYn			;
		private	String	friYn			;
		private	String	satYn			;
		private	String	sunYn			;
		private	String	strtDt			;			//적용일
		private	String	memo			;
		private	String	zoneList		;
		private	String	siDo			;		
		private	String	siGu			;
		
	
	
}