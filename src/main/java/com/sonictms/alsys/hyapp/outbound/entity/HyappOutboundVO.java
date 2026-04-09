package com.sonictms.alsys.hyapp.outbound.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 피킹(출고) 및 재고 관리 통합 VO
 */
@Getter
@Setter
public class HyappOutboundVO {

    // --- 회사 및 센터 정보 ---
    private String outCmpyCd;
    private String cmpyCd;
    private String dcCd;

    // --- 판매 오더 및 상태 ---
    private Long tblSoMId;
    private Long tblSoPId;
    private String soNo;
    private String soStatCd;

    // --- 거래처 및 상품 정보 ---
    private String agntCd;
    private String agntNm;
    private String mtrlCd;
    private String mtrlNm;
    private String barcode;
    private String mto;
    private String mtoYn;

    // --- 배송 및 날짜 정보 ---
    private String postCd;
    private String addr1;
    private String addr2;
    private String smplAddr;
    private String dlvyRqstMsg;
    private String dlvyCnfmDt;
    private String rqstDt;
    private String dlvyDt; // API 파라미터용 추가

    // --- 수량 정보 ---
    private Integer qty;
    private Integer scanQty;
    private Integer totalQty;
    private Integer itemCount;
    private Integer cancelCount;

    // --- 마스터 리스트용 필드 ---
    private String paltNoxx;
    private String dlvyEr;
    private String dlvyTeam;
    private String mobileTranYn;

    // --- 결과 상태 및 메시지 ---
    private String status;
    private String resultMsg;

    // --- 재고(Stock) 및 로케이션 필드 ---
    private Long stockId;
    private String locId;
    private String locCd;
    private String locNm; // 로케이션 명칭
    private Integer stockQty;
    private String expireDate;
    private String zoneId; // 구역 ID
    private String whCd; // 창고 코드

    // --- [추가] 재고 관리 및 보충(Replenishment) 전용 필드 ---
    private Long selectedStockId; // 사용자 선택 재고 ID
    private Long fromStockId;     // 보충 출발지 재고 ID
    private Long toStockId;       // 보충 목적지 재고 ID
    private Integer moveQty;      // 이동(보충) 수량
    private String userId;        // 작업자 ID (updtUser 대용)

    private String fromLocNm;     // 보충 출발지 로케이션명
    private String toLocNm;       // 보충 목적지 로케이션명

    // HyappOutboundVO.java에 추가
    private String histType;       // BACKWARD, CANCEL 등
    private String beforeStatCd;   // 변경 전 상태
    private String beforeStatNm;   // 변경 전 상태명
    private String afterStatNm;    // 변경 후 상태명
    private String beforeGiDt;     // 변경 전 출고일
    private String histSaveTime;   // 이력 저장 시간
    private String histSaveUser;   // 작업자
}