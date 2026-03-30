package com.sonictms.alsys.erp.erp901001.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901001DetailVO  implements Serializable{
		
	private static final long serialVersionUID = 1L;
	
		private	String	cud				;		//신규 수정 삭제 여부
		private	String	alrmAgntlistId	;		//알림톡 고유아이디
		private	String	saveGubn		;
		private	String	cmpyCd			;		//회사코드
		private	String	agntCd			;		//화주사코드
		private	String	userNm			;		//알림톡 받을사람 사용자이름
		private	String	alrmTlkHp		;		//알림톡 연락처
		private	String	alrmTlkYn		;		//알림톡발송여부
		private	String	memo			;		//메모
		private	String	saveUser		;
		private	String	saveTime		;
		private	String	updtAUser		;
		private	String	updtATime		;
		
		private	String	rtnYn			;
		private	String	rtnMsg			;

}