package com.sonictms.alsys.erp.erp105012.entity;

import lombok.Data;

@Data
public class Erp105012VO {
	private String cmpyCd;      // 회사코드
	private String whCd;        // 창고코드
	private String agntCd;      // 화주코드
	private String agntNm;      // 화주명
	private String mtrlCd;      // 상품코드
	private String mtrlNm;      // 상품명
	private String locId;       // 현재 피킹랙 ID
	private String locCd;       // 현재 피킹랙 코드
	private String pickingLocId; // 변경할 피킹랙 ID
	private String fixedLocCd; // 고정 로케이션 코드
	private String updtUser;    // 수정자
}