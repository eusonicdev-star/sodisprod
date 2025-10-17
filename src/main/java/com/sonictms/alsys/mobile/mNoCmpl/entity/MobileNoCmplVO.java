package com.sonictms.alsys.mobile.mNoCmpl.entity;



import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileNoCmplVO extends CommonVO {
		
	private static final long serialVersionUID = 1L;

	
	private	String	instMobileMId	;   //시공모바일 고유ID

	private	String	tblSoMId		;
	private	String	mobileXCmplType		;
	private	String	mobileXCmplTypeNm		;
	private	String	mobileXCmplTxt		;
	private	String	memo			;
	private	String	signImg			;
	private	String	img1			;
	private	String	img2			;
	private	String	img3			;
	private	String	img4			;
	private	String	img5			;
	private	String	img6			;
	private	String	img7			;
	private	String	img8			;
	private	String	img9			;
	private	String	img10			;
	private	String	mobileXCmpleteUseYn			;
	private	String	bigo			;
	private	String	saveUser		;
	private	String	saveTime		;
	private	String	updtUser		;
	private	String	updtTime		;
	private	String	sendProc		;
	private	String	alrmTalkUserid			;
	private	String	messageType		;
	private	String	phn				;
	private	String	profile			;
	private	String	alrmTalkTmp		;
	private	String	tmplid			;
	private	String	tmplnm			;
	private	String	smskind			;
	private	String	smssender		;
	private	String	smslmstit		;
	private	String	smsonly			;
	private	String	alrmTalkUseYn			;
	
	private	String	sendComplete;	//알림톡 발송의 성공 실패, 여부
	private	String	reserveDt;		//전송시간 00000000000000 (14개) 즉시, yyyyMMddHHmmssSSS 지정시간 발송
	private	String	alrmTlkMsg;		//전송한 알람톡 메세지
	private	String	title;			//일림톡 실패시 문자발송할떄 타이틀	
	private	String	message;	//알림톡 발송결과 코드의 결과원인 코드
	private String agntSendYn;           //화주사 알림톡 수신여부
	private String agntSendHp1;           //화주사 알림톡 관리자 전화번호
	
	

}