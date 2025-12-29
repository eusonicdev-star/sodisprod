package com.sonictms.alsys.erp.erp105003.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 입고 완료 처리 VO
 * TBL_INBOUND_PRODUCT 테이블과 매핑
 */
@Getter
@Setter
@ToString
public class Erp105003VO {
    
    // 기본 정보
    private Long tblInboundProductId;
    private String cmpyCd;
    private String agntCd;
    private String agntNm; // 화주사명 (JOIN으로 가져옴)
    private String mtrlCd;
    private String mtrlNm;
    private Long tblMtrlMId;
    
    // 입고 정보
    private Integer inboundQuantity; // 예정 수량
    private String inboundStatus;
    private String expectedDate;
    private String saveTime;
    private String saveUser;
    private String updtTime;
    private String updtUser;
    
    // 실제 입고 정보 (입력받을 필드)
    private Integer actualQuantity; // 실제 입고 수량
    private String actualDate; // 실제 입고 날짜
    private String remarks; // 비고
    
    // 완료 정보
    private String completeTime;
    private String completeUser;
    
    // 되돌리기 정보
    private String revertUser;
    private String deletedYn;
    
    // 검색 조건
    private String dateFrom;
    private String dateTo;
    
    // 페이징 관련 필드
    private int pageNum = 1;
    private int pageSize = 10;
    private int totalCount;
    private int totalPages;
}
