package com.sonictms.alsys.erp.erp901004.entity;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901004VO extends CommonVO {
		
	private static final long serialVersionUID = 1L;

		private	String	prntMtrlList		;	//부모품목코드목록
		private	String	chldMtrlList		;	//자식품목코드목록
		
		private	String	tblBomMId	;	//고유아이디
		private	String	agntCd		;	//화주코드
		private	String	agntNm		;	//화주명칭
		private	String	mtrlCd		;	//부모품목코드
		private	String	mtrlNm		;	//부모품목명칭
		private	String	seq			;	//순번
		private	String	chldMtrlCd		;	//자식품목코드
		private	String	chldMtrlNm		;	//자식품목명칭
		private	String	qty			;	//수량
		private	String	unit		;	//단위
		private	String	tblMtrlMId	;	
}