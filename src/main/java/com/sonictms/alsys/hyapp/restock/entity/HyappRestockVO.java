package com.sonictms.alsys.hyapp.restock.entity;

import lombok.Data;
import java.util.List;

@Data
public class HyappRestockVO {
    // 검색 조건
    private String fromDt;       // 시작일
    private String toDt;         // 종료일
    private String dcCd;         // 물류센터
    private String cmpyCd;       // 회사코드
    private String barcode;      // 스캔된 바코드 (SO_NO)

    // 데이터 필드
    private String tblRestockWaitId; // TBL_SO_P_ID 매핑
    private String soNo;
    private String prodCd;
    private String prodNm;
    private String returnQty;
    private String agntNm;
    private String inboundYn;

    // 처리용 필드
    private String userId;
    private String userName;
    private List<String> selectedIds; // 일괄 처리를 위한 ID 리스트
    private String selectedId;
    private String saveUser;

    private String fixedLocNm; // 권장 로케이션
}