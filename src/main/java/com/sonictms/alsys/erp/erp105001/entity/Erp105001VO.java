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

    // 그리드 표시용 필드
    private String cmpyCd;
    private String agntCd;
    private String agntNm;
    private String productCd;
    private String productNm;
    private Integer totalQty;              // 전체 재고
    private Integer yonginReentryQty;     // 용인센터 재입고대기
    private Integer yonginQty;             // 용인센터 재고
    private Integer gyeongnamQty;         // 경남센터 재고
    private Integer gyeongbukQty;          // 대구센터 재고
    private Integer jeonnamQty;            // 전남센터 재고
    private Integer jejuQty;               // 제주센터 재고
    private Integer availableQty;          // 가용재고
    
    // 재고 조정 관련 필드
    private Long tblInvProductId;
    private String adjustmentType;
    private Integer adjustmentQty;
    private Integer afterQty;
    private String adjustmentReason;
    private String adjustmentDate;
    private Integer currentQty;
    private String saveUser;

    // [추가] 로케이션 재고 탭 관련 필드
    private String whCd;                   // 창고코드
    private String whNm;                   // 창고명
    private String zoneNm;                 // 구역명
    private String locNm;                  // 로케이션명
    private String lotNo;                  // LOT번호
    private Integer stockQty;              // 현재고(로케이션별)
    private Integer availQty;              // 가용재고(로케이션별)
    private String expireDate;             // 유통기한
    private String pltSpec;                // 팔레트규격
}
