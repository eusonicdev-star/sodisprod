package com.sonictms.alsys.erp.erp105011.entity;

import com.sonictms.alsys.common.entity.CommonVO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class Erp105011VO extends CommonVO {

    private static final long serialVersionUID = 1L;

    // 조회 조건
    private String cmpyCd;
    private String agntCd;
    private String zoneNm;
    private String locNm;
    private String mtrlCd;
    private String mtrlNm;

    // 조회 결과 및 재고 정보
    private Long tblMtrlMId;
    private String agntNm;
    private String zoneId;
    private Long locId;
    private String whCd;
    private Integer stockQty;    // 현재고
    private Integer availQty;    // 가용재고
    private String lastInDt;     // 최종입고일

    // 재고 이동(Transfer) 처리용 필드
    private Long fromLocId;      // 출발지 로케이션 ID
    private Long toLocId;        // 목적지 로케이션 ID
    private String toLocNm;      // 목적지 로케이션 명
    private Integer transferQty; // 이동 수량
    private String transferReason; // 이동 사유
    private String processUser;  // 처리자 ID
}