package com.sonictms.alsys.hyapp.inbound.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.time.LocalDate;

@Getter @Setter
public class HyappInboundDTO {
    private Long tblInboundProductId;
    private String cmpyCd;
    private String agntCd;
    private String mtrlCd;
    private String mtrlNm;

    private String zoneId;
    private String zoneCd;
    private String zoneNm;

    private String locBarcode;
    private Integer totalQty;

    private List<PutLine> lines;

    @Getter @Setter
    public static class PutLine {
        private Integer qty;
        private String lotNo;
        private LocalDate expireDate; // "yyyy-MM-dd"면 자동 파싱됨
    }
    private String putawayUser;
}
