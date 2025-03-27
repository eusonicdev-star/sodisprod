package com.sonictms.alsys.mobile.mYesCmpl.entity;

import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper=true)
public class MobileYesCmplVO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private	String	instMobileMId	;   //시공모바일 고유ID
	private	String	tblSoMId		;
	private	String	dlvyCostClct	;
	private	String	dlvyCostClctNm	;
	private	String	dlvyCostTxt		;
	private	String	liftCostYn		;
	private	String	listCostTxt		;
	private	String	downSrvcYn		;
	private	String	downSrvcTxt		;
	private	String	wallFstnYn		;
	private	String	wallFstnTxt		;
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
	private	String	bigo			;
	private	String	sendProc		;
	private	String	alrmTalkUserid	;
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
	private	String	alrmTalkUseYn	;
	
	private	String	sendComplete	;	//알람톡 전송 완료 여부
	private	String	cmplUserid		;	//알림톡 사용자 아이디 alliance
	private	String	reserveDt		;	//알림톡 발송시간
	private	String	alrmTlkMsg		;	//발송한 알림톡 내용
	private	String	title			;	//알림톡 발송 실패시 문자로 발송할떄 문제 제목
	private	String	message			;
	
	//20220224 정연호 추가. 배송기사 평가
	private	String	feedSendProc		;
	private	String	feedAlrmTalkUserid	;
	private	String	feedMessageType		;
	private	String	feedPhn				;
	private	String	feedProfile			;
	private	String	feedAlrmTalkTmp		;
	private	String	feedTmplid			;
	private	String	feedTmplnm			;
	private	String	feedSmskind			;
	private	String	feedSmssender		;
	private	String	feedSmslmstit		;
	private	String	feedSmsonly			;
	private	String	feedAlrmTalkUseYn	;
	private	String	soType				;	//오더유형코드
}