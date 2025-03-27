package com.sonictms.alsys.erp.erp102002.entity;

import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp102002VO extends commonVO{
		
	private static final long serialVersionUID = 1L;
	
		private	String	agntCd			;		//화주(물류센터)코드
		private	String	agntNm			;		//화주(물류센터)명칭
		
		private	String	tblAlrmScdlId	;		//고유아이디
		private	String	alrmMsgType		;		//알람톡 메세지 타입
		private	String	alrmMsgTypeNm	;		//알람톡 메세지 명칭
		private	String	dtCalType		;		//날짜계산조건코드
		private	String	dtCalTypeNm		;		//날짜계산조건명칭
		private	String	dtCalDay		;		//날짜계산일
		private	String	sendTime		;		//전송시간
		private	String	memo			;		//메모

		private	String	execDt		;		//실행일
		
		private	String	tblSoMId		;		//AL 오더 고유 아이디
		private	String	sendProc		;		//전송프로세스
		private	String	userid			;		//알림톡 userid
		private	String	messageType		;		//메세지 타임
		private	String	phn				;		//받는사람 전화번호
		private	String	profile			;		//알림톡 프로필
		private	String	msg				;		//템플릿원본메세지
		private	String	alrmTalkTmp		;		//전송할 메세지
		private	String	tmplid		;			//알림톡 템플릿 아이디
		private	String	reserveDt		;		//발송시간

		private	String	soNo			;		//오더번호
		
		
	
}