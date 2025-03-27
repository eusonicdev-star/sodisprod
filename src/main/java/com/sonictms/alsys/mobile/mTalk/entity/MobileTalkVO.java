package com.sonictms.alsys.mobile.mTalk.entity;



import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileTalkVO extends commonVO{
		
	private static final long serialVersionUID = 1L;
	private String messageType;    //알림톡 발송 메세지 타입
	private String phn;    //알림톡 받을 사람 전화번호
	private String profile;    //알림톡에서 준 고유아이디
	private String tmplId;    //템플릿아이디
	private String msg;    //알림톡용 메세지(템플릿)
	private String smsKind;    //알림톡 실패시 발송할 문자의 종류 문자메시지 전환발송시 SMS/LMS구분 (S:SMS, L:LMS,M:MMS, N:발송하지 않음)
	private String msgSms;       //알림톡 실패시 문자로 발송할 내용(알림톡 내용이랑 같은거 씀)
	private String smsSender;       //알림톡 실패시 문자로 발송할때 발송번호(알림톡 업체에 등록되어있는 번호여야함)
	private String smsLmsTit;   //알림톡 실패시 문자로 발송할때 LMS 일경우 제목
	private String smsOnly;   //Y 면 알림톡으로 안보내고 그냥 문자로만 보냄, N 알림톡으로 발송하고 실패하면 문자로 보냄
	private String code;
	private String msgid;
	private String message;
	private String originMessage;
	private String type;
	
	private String instMobileMId;
	private String dlvyRqstMsg;       //요청사항
	private String cnslTxt;               //상담내용
	private String dlvyDt;                 //배송예정일자
	private String fromDlvyTime;     //배송예정시간 FROM
	private String toDlvyTime;         //배송예정시간 TO
	
	private String alrmTalkTmp;           //메세지(통화내용)
	private String dlvyStatCd;         //배송상태코드


	private String tmplNm;       
	   
	private String tblSoMId;     
	private String sendProc;     
	private String userid;       
	private String talkReserveDt;

    public Data data;
    @Getter
    @Setter
    @ToString(callSuper=true)

    public class Data{
        private String phn;
        private String msgid;
        private String type;
    }
    

    private String sendComplete		;	//알림톡 발송 성공여부
    private String reserveDt		;	//발송시간 0000000 즉시 
    private String alrmTlkMsg		;	//발송한 알림톡 메세지
    private String title			;	//발송한 알림톡 메세지 제목
    private	String	cmplMsgCd		;	//알림톡 발송 결과의 결과 코드 


}