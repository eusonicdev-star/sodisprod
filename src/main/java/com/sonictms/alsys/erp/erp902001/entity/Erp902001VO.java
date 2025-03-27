package com.sonictms.alsys.erp.erp902001.entity;

import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp902001VO extends commonVO{
		
	private static final long serialVersionUID = 1L;
	
		private	String	dcCd			;		//지점코드
		
		private	String	tblLgstDcMId	;		//지점테이블 고유아이디
		private	String	prDcCd			;		//상위지점코드
		private	String	prDcNm			;		//상위지점명칭
		private	String	dcNm			;		//지전명칭
		private	String	dcTypeCd		;		//지점유형코드
		private	String	dcTypeNm		;		//지점유형명칭
		private	String	bossNm		;		//지점장명
		private	String	tel				;		//일반전화
		private	String	hp				;		//휴대전화
		private	String	postCd			;		//우편번호
		private	String	addr			;		//주소1
		private	String	addrDtl			;		//주소2
		private	String	fax				;		//팩스
		private	String	memo			;		//메모

		
	
}