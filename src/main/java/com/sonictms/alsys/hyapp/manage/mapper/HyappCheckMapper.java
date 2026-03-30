package com.sonictms.alsys.hyapp.manage.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HyappCheckMapper {
    /**
     * 전산 재고 조회
     * - TBL_STOCK, TBL_MTRL_M, TBL_WH_LOC_M 조인
     * - 기존 selectRackItemList 쿼리 활용
     */
    List<Map<String, Object>> selectSystemStock(@Param("locCd") String locCd);

    // 실사 저장 (Merge)
    int mergeStockTake(Map<String, Object> params);

    List<Map<String, Object>> selectCheckHistorySummary();

    // 현재 로케이션 실사 목록
    List<Map<String, Object>> selectCheckList(@Param("locCd") String locCd);

    // 상품 정보
    Map<String, Object> selectProductInfo(@Param("barcode") String barcode);

    // 일자별 이력 조회
    List<Map<String, Object>> selectCheckHistory(@Param("date") String date);
}