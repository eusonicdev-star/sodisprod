package com.sonictms.alsys.hyapp.outbound.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 피킹(출고) 리스트용 VO
 *
 * - MyBatis HyappOutboundMapper.xml 의 selectPickListByDlvyCnfmDt 결과 컬럼(alias) 기준으로 매핑됩니다.
 * - JSON 응답으로도 그대로 내려가므로, 화면에서는 이 필드명(camelCase)을 사용하면 됩니다.
 */
@Getter
@Setter
public class HyappOutboundVO {

    // 출고 회사(위탁/외부) 코드
    private String outCmpyCd;

    // 회사 / 센터

    private String cmpyCd;
    private String dcCd;

    // 판매오더
    private Long tblSoMId;
    private String soNo;
    private String soStatCd;

    // 거래처
    private String agntCd;
    private String agntNm;

    // 배송지
    private String postCd;
    private String addr1;
    private String addr2;
    private String smplAddr;
    private String dlvyRqstMsg;

    // 확정일/요청일
    private String dlvyCnfmDt; // YYYYMMDD
    private String rqstDt;     // YYYYMMDD

    // 수량
    private Integer qty;
}
