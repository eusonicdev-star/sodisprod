package com.sonictms.alsys.wh.entity;

import lombok.Data;

@Data
public class WhZoneVO {
    private Long zoneId;
    private String cmpyCd;
    private String whCd;
    private String zoneCd;
    private String zoneNm;
    private String zoneType;
    private String useYn;
    private String saveTime;
    private String saveUser;
    private String updtTime;
    private String updtUser;
    private String deletedYn;
    private Integer viewSeq;
}