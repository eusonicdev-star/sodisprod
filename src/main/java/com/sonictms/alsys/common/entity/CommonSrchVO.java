package com.sonictms.alsys.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CommonSrchVO {
    // 기본 검색 필드
    private String cmpyCd;    // 회사 코드
    private String whCd;      // 창고 코드
    private String keyword;   // 검색어 (코드 또는 명칭)

    // 로케이션 관련 필드
    private Long locId;       // 로케이션 ID
    private String locCd;     // 로케이션 코드
    private String locNm;     // 로케이션 명칭
    private String locType;   // 로케이션 유형

    // 존(Zone) 관련 필드
    private Long zoneId;      // 존 ID
    private String zoneNm;    // 존 명칭

    // 페이징 (필요 시)
    private int pageNum = 1;
    private int pageSize = 100;
}