package com.sonictms.alsys.hyapp.print.entity;

import lombok.Data;

@Data
public class PrintQueueVO {
    private int seq;            // 고유 번호
    private String agntNm;      // 고객명
    private String mtrlNm;      // 상품명
    private String mtrlCd;      // 상품코드
    private String barcode;     // 바코드
    private int qty;            // 출력 수량
    private String status;      // 상태 (0:대기, 1:완료, 9:에러)
    private String reqDt;       // 요청 시간
    private String doneDt;      // 완료 시간
    private String errMsg;      // 에러 메시지
}