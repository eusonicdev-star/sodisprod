package com.sonictms.alsys.hyapp.info.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HyappInvMapper {

    /**
     * 1. 상품 목록 조회 (상단 리스트용)
     * - 반환 필드: CUST_NM (고객사), MTRL_CD (상품코드), MTRL_NM (상품명), QTY (총재고 SUM)
     * - 조건: keyword가 상품코드/명/바코드 중 하나라도 포함되면 검색
     */
    List<Map<String, Object>> selectProductList(
            @Param("keyword") String keyword,
            @Param("hasSpace") boolean hasSpace,
            @Param("tokens") List<String> tokens
    );


    /**
     * 2. 로케이션별 재고 목록 (하단 상세용)
     * - 반환 필드: MTRL_CD (프론트 매핑용), LOC_CD (로케이션), QTY (재고수량)
     * - 조건: 위와 동일한 검색 조건 사용 (상품별로 필터링하기 위해 전체 데이터를 가져오거나, 쿼리에서 조절)
     */
    List<Map<String, Object>> selectLocationListByMtrlCd(@Param("mtrlCd") String mtrlCd);

    // [추가] 랙 목록 (Map으로 반환: ZONE_NM, LOC_CD, TOTAL_QTY, ITEM_COUNT 등)
    List<Map<String, Object>> selectRackList(
            @Param("keyword") String keyword,
            @Param("emptyOnly") boolean emptyOnly
    );

    // [추가] 랙 상세 상품
    List<Map<String, Object>> selectRackItemList(
            @Param("locId") String locId,
            @Param("locCd") String locCd
    );

    List<Map<String, Object>> selectGlobalStockList();
    // ... 기존 코드 아래에 추가

    List<Map<String, Object>> selectCustLocationList(
            @Param("custCd") String custCd,
            @Param("mtrlCd") String mtrlCd
    );
}