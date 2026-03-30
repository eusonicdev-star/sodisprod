package com.sonictms.alsys.hyapp.inbound.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

// 검색 요청용 별도 클래스 (또는 기존 VO 활용)
@Getter
@Setter
public class ProductSearchReq {
    private String cmpyCd;
    private List<String> keywordList; // 여기에 키워드를 담습니다.
}
