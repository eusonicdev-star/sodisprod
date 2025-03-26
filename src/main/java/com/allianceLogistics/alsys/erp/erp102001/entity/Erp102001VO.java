package com.allianceLogistics.alsys.erp.erp102001.entity;

import java.util.List;

import com.allianceLogistics.alsys.common.entity.commonVO;
import com.allianceLogistics.alsys.erp.erp101001.entity.Erp101001VO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Erp102001VO extends commonVO {

  private static final long serialVersionUID = 1L;

  private String id;
  private String dtType; // 달력유형
  private String fromDt; // 시작일
  private String toDt; // 종료일
  private String acptNm; // 고객명
  private String acptTel; // 고객전화번호
  private String agntCd; // 화주사코드
  private String mallNm; // 쇼핑몰

  private String tblSoMId; // 주문헤더고유ID

  private String soNo; // AL 오더
  private String ordrInptDt; // 시스템등록일
  private String rcptDt; // 주문접수일
  private String rqstDt; // 고객배송요청일

  private String agntNm; // 화주사명칭
  private String mallCd; // 쇼핑몰
  private String acptEr; // 수령인
  private String acptTel1; // 수령인전화번호
  private String prodNm; // 품목

  private String brand; // 브랜드

  private String soType;// 주문유형코드
  private String soTypeNm;// 주문유형명칭
  private String soStatCd;// 주문상태코드
  private String soStatNm;// 주문상태명칭

  private String refSoNo;// 고객사 주문번호

  private String dlvyCostType;// 오더상태코드
  private String dlvyCostTypeNm;// 오더상태명칭
  private String rcptCost;// 착불비
  private String passCost;// 통행료
  private String dlvyCnfmDt;// 배송일

  private String dlvyRqstMsg;// 배송요청사항
  private String custSpclTxt;// 고객사특이사항
  private String dcCd;// 물류센터코드
  private String dcNm;// 물류센터명칭
  private String fromEr;// 발송인이름
  private String fromTel1;// 발송인일반전화
  private String fromTel2;// 발송인휴대전화
  private String fromPostCd;// 발송인우편번
  private String fromAddr1;// 발송인주소1
  private String fromAddr2;// 발송인주소2(상세주소)

  private String acptTel2;// 수취인 휴대전화
  private String postCd;// 수취인우편번호
  private String addr1;// 수취인주소1
  private String addr2;// 수취인주소2(상세주소)
  private String distChanCd;
  private String dlvyLiftDt;// 공장출고일
  private String orgnSoNo;// 원 AL 오더

  private String delResn; // 삭제이유
  private String memo; // 비고

  private String tblSoPId; //

  private String prodSeq; // 품목순번
  private String prodCd; // 품목코드

  private String qty; // 수량
  private String instCost; // 시공비

  private String totCnt; // 총 상담건수
  private String tblSoCnslMId; // 상담고유ID
  private String seq; // 상담순번
  private String cnslEr; // 최근 상담내역 - 상담한 사람
  private String cnslTxt; // 최근 상담내역 - 상담한 내용
  private String cnslTime; // 최근 상담내역 - 상담한 시간

  private String message_type; // 발송종류
  private String instMobileMId; // 모바일 고유아이디

  private String dlvyDt; // 배송예정일자
  private String fromDlvyTime; // 배송예정시간 FROM
  private String toDlvyTime; // 배송예정시간 TO

  private String alrmTalkTmp; // 메세지(통화내용)
  private String dlvyStatCd; // 배송상태코드

  private String messageType; // 알림톡 발송 메세지 타입
  private String phn; // 알림톡 받을 사람 전화번호
  private String profile; // 알림톡에서 준 고유아이디
  private String tmplId; // 템플릿아이디
  private String tmplNm; // 템플릿명칭
  private String msg; // 알림톡용 메세지(템플릿)
  private String smsKind = "L"; // 알림톡 실패시 발송할 문자의 종류 문자메시지 전환발송시 SMS/LMS구분 (S:SMS, L:LMS,M:MMS, N:발송하지 않음)
  private String msgSms; // 알림톡 실패시 문자로 발송할 내용(알림톡 내용이랑 같은거 씀)
  private String smsSender = "0315269848"; // 알림톡 실패시 문자로 발송할때 발송번호(알림톡 업체에 등록되어있는 번호여야함)   //20230401 정연호. 0315269846 을 0315269848 로 변경
  private String smsLmsTit; // 알림톡 실패시 문자로 발송할때 LMS 일경우 제목
  private String smsOnly = "N"; // Y 면 알림톡으로 안보내고 그냥 문자로만 보냄, N 알림톡으로 발송하고 실패하면 문자로 보냄

  private String type; // 알림톡 유형

  private String sendComplete; // 발송결과 success, fail 두개중에 하나만 넘어옴

  private String sendProc; // 알림톡 발송-단계 명칭
  private String userid = "alliance"; // 알림톡 발송-발송자아이디
  private String talkReserveDt; // 알림톡 발송-발송할시간

  private String code; // 리턴값 //발송결과 success, fail 두개중에 하나만 넘어옴
  private String msgid; // 리턴값
  private String message; // 리턴값
  private String originMessage; // 리턴값

  public Data data;

  @Getter
  @Setter
  @ToString(callSuper = true)

  public class Data {
    private String phn; // 리턴값
    private String msgid; // 리턴값
    private String type; // 리턴값
  }

  private String instPlanDt; // 배송계획일자
  private String instDt; // 시공일자
  private String instStatCd; // 시공계획상태코드
  private String instStatNm; // 시공계획상태명칭

  private String dlvyStatNm; // 배송상태명칭
  private String paltNoxx; // 팔렛트번호

  private String instEr; // 시공기사명
  private String hp; // 시공기사 핸드폰
  private String xcmplType; // 미마감유형코드
  private String xcmplTypeNm; // 미마감사유
  private String signImg; // 사인이미지
  private String img1;
  private String img2;
  private String img3;
  private String img4;
  private String img5;
  private String img6;
  private String img7;
  private String img8;
  private String img9;
  private String img10;

  private	String	updtEventTxt;	//변경이벤트
  private	String	updtBeforeTxt;	//변경전값
  private	String	updtAfterTxt;	//변경후값
  private	String	saveDate;	//변경시간


  private String dlvyinfo			;
  private String dlvyCostClct		;
  private String dlvyCostClctNm	;
  private String dlvyCostTxt		;
  private String liftCostYn		;
  private String listCostTxt		;
  private String downSrvcYn		;
  private String downSrvcTxt		;
  private String wallFstnYn		;
  private String wallFstnTxt		;
  private String dlvyCnfmMemo		;
  private String dlvyCnfmSign		;
  private String dlvyCnfmImg1		;
  private String dlvyCnfmImg2		;
  private String dlvyCnfmImg3		;
  private String dlvyCnfmImg4		;
  private String dlvyCnfmImg5		;
  private String dlvyCnfmImg6		;
  private String dlvyCnfmImg7		;
  private String dlvyCnfmImg8		;
  private String dlvyCnfmImg9		;
  private String dlvyCnfmImg10	;

  private String xcmplMemo		;
  private String xcmplSign		;
  private String xcmplImg1		;
  private String xcmplImg2		;
  private String xcmplImg3		;
  private String xcmplImg4		;
  private String xcmplImg5		;
  private String xcmplImg6		;
  private String xcmplImg7		;
  private String xcmplImg8		;
  private String xcmplImg9		;
  private String xcmplImg10		;

  //20211128 정연호 추가
  private	String	srchAddr;			//주소검색

  //20211231 정연호 추가
  private	String	dlvyCost		;		//물류비
  private	String	refSoList		;
  private	String	agntList		;		//화주

  private	String	dcList			;		//물류센터
  private	String	soList			;		//오더번호목록
  private	String	soTypeList		;		//오더유형
  private	String	soStatList		;		//오더상태

  private	String	ordrEr			;		//주문자

  private	String	srchDay			;		//찾을날짜 범위
  private	String	prodCdList		;		//상품코드 리스트
  private	String	qtyList			;		//수량리스트
  private	String	addr			;		//주소

  //배송확정일 가능한 날짜 관련 
  private	String	capaId		;		//좌석아이디
  private	String	weekCd		;		//요일코드
  private	String	weekNm		;		//요일명칭
  private	String	capaCtgr	;		//시공카테고리코드
  private	String	capaCtgrNm	;		//시공카테고리명칭
  private	String	capaType	;		//시공유형코드
  private	String	capaTypeNm	;		//시공유형명칭
  private	String	zoneType	;		//권역유형코드
  private	String	zoneTypeNm	;		//권역유형명칭
  private	String	zoneCd		;		//권역코드
  private	String	zoneCdNm	;		//권역명칭
  private	String	ableYn		;		//날짜 사용가능
  private	String	totCapa		;		//총시공케파
  private	String	useCapa		;		//사용한케파
  private	String	remnCapa	;		//남은케파


  private	List<Erp101001VO>	detailData;

  private	String	costType	;		//운임구분
  private	String	inptSys		;		//입력시스템 (1건식 입력 ERP_SO, 엑셀로 한꺼번에 입력 ERP_EXCEL)

  private	String	rtnSoType	;
  private String	rtnName;
  private	String	giLiftDt		;		//공장출고일
  //20211231 정연호 추가. 끝

  //20220106 정연호 시공카테고리
  private	String	srchInstCapaCtgr;
  private	String	instCtgrList;
  private	String	instCtgr;
  private	String	instCtgrNm;

}