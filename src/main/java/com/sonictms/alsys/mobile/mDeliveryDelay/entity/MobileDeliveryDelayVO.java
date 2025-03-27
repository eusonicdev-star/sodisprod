package com.sonictms.alsys.mobile.mDeliveryDelay.entity;



import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileDeliveryDelayVO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private	String	instMobileMId	;   //시공모바일 고유ID
	private	String	tblSoMId	    ;   //주문 고유 ID
	private	String	capaId	        ;   //캐파 고유 ID
	private	String	capaType	    ;   //캐파 유형
	private	String	zoneType	    ;   //권역유형
	private	String	zoneCd	        ;   //권역코드
	private	String	capaSeatType	;   //캐파 좌석 유형 1111 1인시공 2222 2인시공
	private	String	instDt	        ;   //시공일
	private	String	weekCd	        ;   //요일 코드
	private	String	totCapa	        ;   //총캐파
	private	String	useCapa	        ;   //사용캐파
	private	String	remnCapa	    ;   //남은캐파
	private	String	statCd	        ;   //배송상태코드
	private	String	ableYn	        ;   //사용가능여부
	private	String	dlvyCnclDt	    ;   //배송일(원래배송일) 년월일
	private	String	dlvyCnclDt2	    ;   //배송일(원래배송일)
	private	String	memo	        ;   //메모
	
	private	String	orgnDlvyDt	    ;   //원래배송일자
	private	String	postPoneDt	    ;   //변경한배송일자
	private	String	postPoneMemo	;   //변경사유
	
	//20220517 정연호 추가
	private	String	capaCtgr;
}