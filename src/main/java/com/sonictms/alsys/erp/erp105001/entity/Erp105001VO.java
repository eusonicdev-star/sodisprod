package com.sonictms.alsys.erp.erp105001.entity;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Erp105001VO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tblInvProductId;  // TBL_MTRL_M_ID로 사용
    private String cmpyCd;
    private String agntCd;
    private String agntNm;
    private String productCd;
    private String productNm;
    private Integer totalQty;              // 전체 재고
    private Integer yonginReentryQty;     // 용인센터 재입고 대기
    private Integer gyeongnamQty;         // 경남센터
    private Integer gyeongbukQty;          // 경북센터
    private Integer jeonnamQty;            // 전남센터
    private Integer jejuQty;               // 제주센터
    private Integer yonginQty;             // 용인센터
    
    // 재고 조정 관련 필드
    private String adjustmentType;  // INCREASE, DECREASE
    private Integer adjustmentQty;  // 조정 수량
    private Integer afterQty;       // 조정 후 수량
    private String adjustmentReason; // 조정 사유
    private String adjustmentDate;   // 조정 일자
    private Integer currentQty;      // 조정 시 현재 재고 (기존 필드 유지)
}
