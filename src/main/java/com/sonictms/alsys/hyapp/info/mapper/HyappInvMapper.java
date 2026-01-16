package com.sonictms.alsys.hyapp.info.mapper;

import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.info.service.HyappInvService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HyappInvMapper {

    /**
     * 상품 후보 조회 (상세 화면용)
     * - mtrlCd / mtrlNm / barcode 조건을 AND로 적용
     * - Service에서 다건 여부 판단을 위해 최대 2건만 가져오도록 SQL에서 LIMIT 2 권장
     */
    public List<HyappInvService.ProductRow> findProducts(
            @Param("mtrlCd") String mtrlCd,
            @Param("mtrlNm") String mtrlNm,
            @Param("barcode") String barcode
    );

    /**
     * 상품 전체 재고 수량(합계)
     */
    public Long selectTotalQty(
            @Param("mtrlCd") String mtrlCd,
            @Param("mtrlNm") String mtrlNm,
            @Param("barcode") String barcode
    );

    /**
     * 로케이션별 재고 목록
     * - 존명/로케이션/수량/로트/유통기한
     */
    public List<HyappInvService.LocationStockRow> selectLocStocks(
            @Param("mtrlCd") String mtrlCd,
            @Param("mtrlNm") String mtrlNm,
            @Param("barcode") String barcode
    );

}
