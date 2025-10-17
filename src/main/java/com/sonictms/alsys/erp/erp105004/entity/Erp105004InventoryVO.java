package com.sonictms.alsys.erp.erp105004.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Erp105004InventoryVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long tblInvProductId;
    private String cmpyCd;
    private String agntCd;
    private String agntNm;
    private Long tblMtrlMId;
    private String mtrlCd;
    private String mtrlNm;
    private Integer currentQty;
    private LocalDateTime saveTime;
    private String saveUser;
    private LocalDateTime updtTime;
    private String updtUser;
    
    // 페이징 관련 필드
    private int pageNum = 1;
    private int pageSize = 10;
    private int totalCount;
    private int totalPages;
}
