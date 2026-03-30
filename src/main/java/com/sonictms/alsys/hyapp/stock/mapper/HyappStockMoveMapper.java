package com.sonictms.alsys.hyapp.stock.mapper;

import com.sonictms.alsys.hyapp.stock.entity.HyappStockMoveVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface HyappStockMoveMapper {

    // 로케이션별 재고 조회
    List<Map<String, Object>> selectStockListByLocation(String cmpyCd, String locationCd);

    // 출발지 재고 차감 (성공 시 1 반환)
    int updateDecreaseStock(HyappStockMoveVO moveVO);

    // 목적지 재고 가산 (Merge 사용)
    int upsertIncreaseStock(HyappStockMoveVO moveVO);

    // 이동 이력 저장
    int insertMoveLog(HyappStockMoveVO moveVO);
}