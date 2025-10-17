package com.sonictms.alsys.erp.erp105005.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Erp105005VO extends CommonVO {

    private static final long serialVersionUID = 1L;

    // 기본 정보
    private String tblReturnInboundId;     // 재입고 추적 ID
    private String tblSoMId;              // 주문 헤더 ID
    private String soNo;                  // 주문번호
    private String soType;                // 주문유형
    private String soTypeNm;              // 주문유형명
    private String soStatCd;              // 주문상태
    private String soStatCdNm;            // 주문상태명
    private String prodCd;                // 상품코드
    private String prodNm;                // 상품명
    private String returnQty;             // 반품/교환 수량
    private String instEr;

    // 재입고 정보
    private String inboundYn;             // 재입고여부 (Y/N)
    private String inboundDt;             // 재입고일
    private String inboundUser;           // 재입고처리자
    private String inboundTime;           // 재입고처리시간

    // 반출 정보
    private String outboundYn;            // 반출여부 (Y/N)
    private String outboundDt;            // 반출일
    private String outboundUser;          // 반출처리자
    private String outboundTime;          // 반출처리시간
    private String outboundTargetYn;      // 반출대상여부 (Y/N)

    // 주문 정보 (TBL_SO_M에서 조인)
    private String agntCd;                // 화주코드
    private String agntNm;                // 화주명
    private String acptEr;                // 수취인
    private String acptTel1;              // 수취인 전화번호
    private String acptTel2;              // 수취인 휴대전화
    private String postCd;                // 우편번호
    private String addr1;                 // 주소1
    private String addr2;                 // 주소2
    private String dcCd;                  // 물류센터코드
    private String dcNm;                  // 물류센터명
    private String ordrInptDt;            // 주문입력일
    private String rcptDt;                // 접수일
    private String rqstDt;                // 배송요청일
    private String dlvyCnfmDt;            // 배송확정일
    private String mallCd;                // 쇼핑몰코드
    private String brand;                 // 브랜드
    private String refSoNo;               // 고객사 주문번호
    private String dlvyRqstMsg;           // 배송요청사항
    private String custSpclTxt;           // 고객특이사항
    private String saveTime;              // 저장일시

    // 검색 조건
    private String dtType;                // 달력유형 (1:주문접수일, 2:배송요청일, 3:배송확정일)
    private String fromDt;                // 검색 시작일
    private String toDt;                  // 검색 종료일
    private String agntList;              // 화주사 목록
    private String dcList;                // 물류센터 목록
    private String soList;                // 주문번호 목록
    private String soTypeList;            // 주문유형 목록
    private String acptNm;                // 수취인명
    private String acptTel;               // 수취인 전화번호
    private String prodList;              // 상품코드 목록
    private String prodNmSrch;            // 상품명 검색
    private String srchAddr;              // 주소 검색
    private String inboundStatus;         // 재입고 상태 (ALL, Y, N)
    private String outboundStatus;        // 반출 상태 (ALL, Y, N)
    private String outboundTarget;        // 반출 대상 여부 (ALL, Y, N)

    // 처리용
    private String[] selectedIds;         // 선택된 ID 목록
    private String processType;           // 처리 유형 (INBOUND, OUTBOUND)
    private String processUser;           // 처리자
    private String processDt;             // 처리일
}

