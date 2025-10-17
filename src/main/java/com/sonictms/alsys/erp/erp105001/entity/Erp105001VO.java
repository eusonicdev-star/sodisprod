package com.sonictms.alsys.erp.erp105001.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Erp105001VO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tblInvProductId;
    private String cmpyCd;
    private String agntCd;
    private String agntNm;
    private Long tblProductMId;
    private String productCd;
    private String productNm;
    private Integer currentQty;
    private LocalDateTime saveTime;
    private String saveUser;
    private LocalDateTime updtTime;
    private String updtUser;
    
    // 재고 조정 관련 필드
    private String adjustmentType;  // INCREASE, DECREASE
    private Integer adjustmentQty;  // 조정 수량
    private Integer afterQty;       // 조정 후 수량
    private String adjustmentReason; // 조정 사유
    private String adjustmentDate;   // 조정 일자
}
