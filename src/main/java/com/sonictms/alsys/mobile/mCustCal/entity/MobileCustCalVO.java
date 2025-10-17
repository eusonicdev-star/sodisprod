package com.sonictms.alsys.mobile.mCustCal.entity;



import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@ToString(callSuper=true)
public class MobileCustCalVO extends CommonVO {
		
	private static final long serialVersionUID = 1L;

	private	String	soNo;			//AL 오더
	
	private	String	capaId;
	private	String	capaType;
	private	String	capaCtrg;
	private	String	zoneType;
	private	String	zoneCd;				
	private	String	instDt;				//날짜
	private	String	weekCd		;		//요일코드
	private	String	totCapa;			//총캐파
	private	String	useCapa;			//사용캐파
	private	String	remnCapa;			//남은캐파
	private	String	ableYn;				//사용가능한 날짜 여부
	
	private	String	dlvyAsgnDt	;	//이미 지정된 기본 배송일자 년 월 일 표시
	
	//20211231 정연호 이미 지정된 기본 배송일자 숫자 8자리
	private	String	dlvyAsgnDt2	;
	
	//20211231 정연호 추가 지정된 배송일의 캐파 아이디
	private	String	dlvyAsgnCapaId;
	
	//20220221 정연호. TBL_SO_M_ID 추가
	private	String	tblSoMId;
	
	//20220224 정연호 추가. 피드백 저장
	private	String	evalItem1	;   //만족-시간약속을 잘지켜요 Y/N
	private	String	evalItem2	;   //만족-친절하고 매너가 좋아요 Y/N
	private	String	evalItem3	;   //만족-주변정리를 잘해주셨어요 Y/N
	private	String	evalItem4	;   //사용안하는 값
	private	String	evalItem5	;   //사용안하는 값
	private	String	evalItem6	;   //사용안하는 값
	private	String	evalItem7	;   //사용안하는 값
	private	String	evalItem8	;   //사용안하는 값
	private	String	evalItem9	;   //사용안하는 값
	private	String	evalItem10	;   //사용안하는 값
	private	String	evalSpcl1	;   //별점 0전부터 0.5점 단위로 5점까지
	private	String	evalSpcl2	;   //사용안하는 값
	private	String	evalSpcl3	;   //사용안하는 값
	private	String	evalSpcl4	;   //사용안하는 값
	private	String	evalSpcl5	;   //사용안하는 값
	private	String	evalMsg		;   //전달하고싶은 의견
}