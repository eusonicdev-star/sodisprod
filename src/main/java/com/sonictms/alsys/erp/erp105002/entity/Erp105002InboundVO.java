package com.sonictms.alsys.erp.erp105002.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Erp105002InboundVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tblInboundProductId;
    private String cmpyCd;
    private String agntCd;
    private String agntNm;
    private String mtrlCd;
    private String mtrlNm;
    private Long tblMtrlMId;
    private Integer inboundQuantity;
    private String inboundStatus;
    private LocalDate expectedDate;
    private LocalDateTime saveTime;
    private String saveUser;
    private LocalDateTime updtTime;
    private String updtUser;
    private Integer actualQuantity;
    private LocalDate actualDate;
    private String remarks;
    private LocalDateTime completeTime;
    private String completeUser;
    private String deletedYn;
    private String dateFrom;
    private String dateTo;
    
    // 페이징 관련 필드
    private int pageNum = 1;
    private int pageSize = 10;
    private int totalCount;
    private int totalPages;
}
