package com.sonictms.alsys.commCode.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class SendAlrmTalkVO extends CommonVO {

	private static final long serialVersionUID = 1L;

	private String message_type;           //발송종류
	private String instMobileMId;           //모바일 고유아이디
	private String dlvyRqstMsg;             //요청사항
	private String cnslTxt;                 //상담내용
	private String dlvyDt;                  //배송예정일자
	private String fromDlvyTime;            //배송예정시간 FROM
	private String toDlvyTime;              //배송예정시간 TO
	private String alrmTalkTmp;             //메세지(통화내용)
	private String dlvyStatCd;              //배송상태코드
	private String messageType;             //알림톡 발송 메세지 타입
	private String phn;                     //알림톡 받을 사람 전화번호
	private String profile;                 //알림톡에서 준 고유아이디
	private String tmplId;                  //템플릿아이디
	private String tmplNm;                  //템플릿명칭
	private String msg;                 //알림톡용 메세지(템플릿)
	private String smsKind;                 //알림톡 실패시 발송할 문자의 종류 문자메시지 전환발송시 SMS/LMS구분 (S:SMS, L:LMS,M:MMS, N:발송하지 않음)
	private String msgSms;                  //알림톡 실패시 문자로 발송할 내용(알림톡 내용이랑 같은거 씀)
	private String smsSender;               //알림톡 실패시 문자로 발송할때 발송번호(알림톡 업체에 등록되어있는 번호여야함)
	private String smsLmsTit;               //알림톡 실패시 문자로 발송할때 LMS 일경우 제목
	private String smsOnly;                 //Y 면 알림톡으로 안보내고 그냥 문자로만 보냄, N 알림톡으로 발송하고 실패하면 문자로 보냄
	private String type;                    //알림톡 유형
	private String tblSoMId;                //주문오더 고유 아이디
	private String sendProc;                //알림톡 발송-단계 명칭
	private String userid;                  //알림톡 발송-발송자아이디
	private String talkReserveDt;           //알림톡 발송-발송할시간
	private String code;                    //리턴값
	private String msgid;                   //리턴값
	private String message;                 //리턴값
	private String originMessage;           //리턴값
	private Object button1;
	private Object button2;
	private String button3;
	private String button4;
	private String button5;
	private String cmplMsgCd;
	public Data data;

	@Getter
	@Setter
	@ToString(callSuper = true)
	public class Data {
		private String phn;     //리턴값
		private String msgid;   //리턴값
		private String type;    //리턴값
	}
}