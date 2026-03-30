package com.sonictms.alsys.hyapp.stock.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class HyappStockMoveVO {
    private String cmpyCd;
    private String agntCd;
    private String mtrlCd;

    private String fromLocationCd; // 출발지
    private String toLocationCd;   // 목적지
    private int moveQty;           // 이동 수량

    private String userId;         // 작업자
    private String moveType;       // 이동 유형 (예: NORMAL)
}