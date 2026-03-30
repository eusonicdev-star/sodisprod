package com.sonictms.alsys.hyapp.inbound.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InboundProductSearchDTO {
    private Long tblMtrlMId;
    private String agntNm;
    private String agntCd;
    private String mtrlNm;
    private String mtrlCd;
    private String barcode;
}