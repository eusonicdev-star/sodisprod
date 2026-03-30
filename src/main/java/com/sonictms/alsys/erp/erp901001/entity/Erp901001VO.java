package com.sonictms.alsys.erp.erp901001.entity;

import java.util.List;

import com.sonictms.alsys.common.entity.CommonVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
//@ToString
@ToString(callSuper=true)
public class Erp901001VO extends CommonVO {
		
	private static final long serialVersionUID = 1L;
	
	private	String	agntCd			;		//화주(물류센터)코드
	private	String	agntNm			;		//화주(물류센터)명칭
	
	private	String	tblAgencyMId	;		//화주테이블 고유아이디
	private	String	bizNo			;		//사업자번호
	private	String	postCd			;		//우편번호
	private	String	addr			;		//주소1
	private	String	addrDtl			;		//주소2
	private	String	bossNm			;		//지점장명
	private	String	bossTel			;		//지점장전화번호
	private	String	officeTel		;		//사무실번호
	private	String	fax				;		//팩스번호
	private	String	mngeNm			;		//관리자이름
	private	String	mngeTel			;		//관리자일반전화
	private	String	mngeHp			;		//관리자휴대전화
	private	String	memo			;		//메모

	private	String	alrmTlkYn		;		//알림톡발송여부
	private	String	alrmTlkHp		;		//알림톡 연락처

	//20220124 정연호 추가
	private	List<Erp901001DetailVO>	alrmTlkDetail;
}