package com.allianceLogistics.alsys.erp.erp902004.entity;

import com.allianceLogistics.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp902004VO extends commonVO{
		
	private static final long serialVersionUID = 1L;

		private	String	chkYn			;		//매핑여부

		private	String	tblPostMId		;
		private	String	postCd		;
		private	String	sidoNm		;
		private	String	siguNm		;
		private	String	dngNm		;
		private	String	riNm		;
		private	String	bunji		;
		private	String	ho		;
		private	String	roadNm		;
		private	String	tblLgstDcpostMId		;
		private	String	cmpyCd		;
		private	String	dcCd		;		;
		private	String	dcNm		;
		private	String	memo;
	
}