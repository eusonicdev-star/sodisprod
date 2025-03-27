package com.sonictms.alsys.erp.erp902003.entity;

import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp902003VO extends commonVO{
		
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
		private	String	tblLgstZonepostMId		;
		private	String	cmpyCd		;
		private	String	zoneType		;
		private	String	zoneTypeNm		;
		private	String	zoneCd		;		;
		private	String	zoneNm		;
		private	String	memo;
	
}