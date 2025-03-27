package com.sonictms.alsys.mobile.mDelivery.entity;



import com.sonictms.alsys.common.entity.commonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileDeliveryVO extends commonVO{
		
	private static final long serialVersionUID = 1L;

	private String  instDt;    //시공일
	private String  tblUserMId;    //user 의 고유ID
	private	String	userGrntCd	;	//사용자권한코드

	private	String	userGrntNm	;	//사용자권한명칭
	private	String	userGrdCd	;	//사용자 등급
	private	String	userGrdNm	;	//사용자 등급
	private	String	dcCd	;       //물류센터코드
	private	String	dcNm	;       //물류센터명칭

	private	String	instMobileMId	;   //시공모바일 고유ID
	private	String	totCnt			;   //전체수량
	private	String	ingCnt			;   //배송중수량
	private	String	ncmplCn			;   //미마감수량
	private	String	cmplCnt			;      //배송완료 수량
	private	String	paltNoxx		;      //팔렛트넘버
	private	String	seq				;       //리스트 맨 앞에 나오는 순번
	private	String	instCtgr		;       //시공카테고리코드-가구/가전/서비스
	private	String	instCtgrNm		;       //시공카테고리명칭
	private	String	instType		;       //시공유형코드    -일반 / 익일/ 새벽
	private	String	instTypeNm		;       //시공유형명칭
	private	String	seatType		;       //시공좌석유형코드  -1인 / 2인
	private	String	seatTypeNm		;       //시공좌석유형명칭
	private	String	dlvyStatCd		;       //배송상태코드
	private	String	dlvyStatNm		;       //배송상태명칭
	private	String	soType			;       //주문유형코드
	private	String	soTypeNm		;       //주문유형명칭
	private	String	soNo			;       //AL 오더(오더번호)
	private	String	acptEr			;       //수취인
	private	String	postCd			;       //우편번호
	private	String	addr1			;       //주소1
	private	String	addr2			;       //주소2(상세주소)
	private	String	acptTel1		;       //수취인 일반번호
	private	String	acptTel2		;       //수취인 휴대전화
	private	String	telCnt			;       //전화한 횟수
	private	String	zoneType		;       //권역유형코드
	private	String	zoneTypeNm		;       //권역유형명칭
	private	String	zoneCd			;       //권역코드
	private	String	zoneNm			;       //권역명칭
	private	String	instStatCd		;       //시공상태코드
	private	String	instCost		;       //시공비
	private	String	dlvyCostType	;       //운임구분코드(착불이냐 선불이냐)
	private	String	rcptCost		;       //착불비
	private	String	passCost		;       //통행료
	private	String	dlvyCost		;       //배송비
	private	String	agntHp			;       //영업자 핸드폰
	private	String	agntCd			;       //영업자 코드
	private	String	dlvyTypeCd		;       //배송유형코드
	private	String	instPlanId		;       //시공계획고유ID
	private	String	tblSoMId		;       //주문 고유ID
	private	String	ordrEr			;       //주문자
	private	String	dlvyRqstMsg		;       //배송요청사항
	private	String	custSpclTxt		;       //고객특이사항
	private	String	cbm				;       //CBM
	private	String	liftCmplYn		;       //상차완료여부
	private	String	hpclCmplYn		;       //해피콜여부
	private	String	dlvyCmplYn		;       //배송완료여부
	private	String	dlvyClclYn		;       //배송취소여부
	private	String	dlvyPstpYn		;       //배송연기유무
	private	String	dlvyNcmplYn		;       //배송미완료유무
	private	String	instMobileDId	;
	private	String	prodCd			;		//품목코드
	private	String	prodNm			;		//품목명칭
	private	String	qty				;		//품목수량

	private	String	instUrl			;		//품목 설명 URL
	private	String	instImgPath		;		//품목설명 이미지
	private	String	liftQty			;		//품목상차수량
	private	String	prodStatCd		;		//품목상태코드
	private	String	prodStatNm		;		//품목상태명칭
	private	String	prodLiftCmplYn	;		//품목상차완료여부

	private	String	editYn	;		//입력 가능한지, 배송일+1일의 10시가 지나서 입력이 불가능 한지를 표시. Y 입력-상차, 미마감,배송완료 등 가능 N 불가능
	private	String	mtoYn	;		//mto여부
	
	private	String	mobileGrntCd	;	//모바일 권한
	
	//20220319 정연호 추가. 배송기사 이름 추가
	private	String	execUserNm;
}