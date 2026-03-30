package com.sonictms.alsys.wh.entity;

import lombok.Data;

import java.util.Date;

@Data
public class WhLocVO {
    private Long locId;           // LOC_ID
    private String cmpyCd;        // CMPY_CD
    private String whCd;          // WH_CD
    private Long zoneId;          // ZONE_ID
    private String locCd;         // LOC_CD
    private String locNm;         // LOC_NM

    // JOIN 필드 추가
    private String zoneCd;        // TBL_WH_ZONE_M.ZONE_CD
    private String zoneNm;        // TBL_WH_ZONE_M.ZONE_NM

    private String locType;       // LOC_TYPE
    private String aisle;         // AISLE
    private String rack;          // RACK
    private String levelNo;       // LEVEL_NO
    private String binNo;         // BIN_NO
    private String useYn;         // USE_YN
    private String pickingYn;     // PICKING_YN
    private String saveUser;      // SAVE_USER
    private Date saveTime;        // SAVE_TIME
    private String deletedYn;        // SAVE_TIME
}