package com.sonictms.alsys.hyapp.common.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

/**
 * 입고 완료 처리 VO
 * TBL_INBOUND_PRODUCT 테이블과 매핑
 */
@Getter
@Setter
@ToString
public class HyappCommonVO {

    private String dcCd;
    private String dcNm;
    private String cmpyCd;
}
