package com.sonictms.alsys.mobile.mBarCode.entity;



import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileBarCodeVO extends CommonVO {
		
	private static final long serialVersionUID = 1L;
	
	private String  barcode;                //바코드 (판마오더번호)
	private	String	instMobileMId;          //시공 모바일 헤더 아이디
	private	String	soNo ;                  //판매오더번호
	private	String	execUserMId  ;          //시공하는 사람 고유아이디
	private	String	userId     ;            //사용자아이디
	private	String	userNm     ;            //이름
	private	String	hp ;                    //핸드폰번호
	private	String	dlvyStatCd   ;          //배송상태코드
	private	String	instStatNm   ;          //배송상태명칭



}