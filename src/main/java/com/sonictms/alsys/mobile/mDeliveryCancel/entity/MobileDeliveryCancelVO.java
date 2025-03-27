package com.sonictms.alsys.mobile.mDeliveryCancel.entity;



import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileDeliveryCancelVO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private	String	instMobileMId	;   //시공모바일 고유ID
	private	String	tblSoMId	    ;   //주문 고유 ID
	private	String	dlvyCnclResn	;   //배송 취소 이유 코드
	private	String	cnclMemo	    ;   //취소 메모
	
}