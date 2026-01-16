package com.sonictms.alsys.hyapp.inbound.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDate;
import java.util.List;

/**
 * 입고 완료 처리 VO
 * TBL_INBOUND_PRODUCT 테이블과 매핑
 */
@Getter
@Setter
@ToString
public class HyappInboundVO {

    private List<HyappInboundVO> list;

    // 기본 정보
    private Long tblInboundProductId;
    private String cmpyCd;
    private String agntCd;
    private String agntNm; // 화주사명 (JOIN으로 가져옴)
    private String mtrlCd;
    private String mtrlNm;
    private Long tblMtrlMId;
    private String barcode;

    // 입고 정보
    private Integer inboundQuantity; // 예정 수량
    private String inboundStatus;
    private String expectedDate;
    private String saveTime;
    private String saveUser;
    private String updtTime;
    private String updtUser;

    // 실제 입고 정보 (입력받을 필드)
    private Integer actualQuantity; // 합격수량 - 실제 입고 수량
    private Integer totalPutawayQty; // 적치완료수량
    private Integer remainingQty; // 적치 가능 수량
    private String actualDate; // 실제 입고 날짜
    private String remarks; // 비고

    // ? 검수 입력(프론트에서 전달)
    private String vhclTonType; // 차량톤수
    private String inMethod;    // BULK | PALLET
    private Integer palletQty;  // 파렛트 수량
    private Integer failQty;    // 불합격 수량
    private String memo;        // 화면 메모(remarks로 변환 저장)

    // 완료 정보
    private String completeTime;
    private String completeUser;

    // 검색 조건
    private String dateFrom;
    private String dateTo;

    // ===== query conditions =====
    private String date;                // 단일 날짜 필터 (yyyy-MM-dd)
    private String keyword;             // 검색어

    // Zone
    private String zoneId;                // ZONE_ID
    private String zoneCd;             // ZONE_CD
    private String zoneNm;             // ZONE_NM
    private Long putId;
    private String whCd;
    private Long locId;
    private String locCd;
    private Integer putawayQty;
    private String lotNo;
    private LocalDate expireDate;
    private String putawayUser;

    // 페이징 관련 필드
    private int pageNum = 1;
    private int pageSize = 10;
    private int totalCount;
    private int totalPages;
}
