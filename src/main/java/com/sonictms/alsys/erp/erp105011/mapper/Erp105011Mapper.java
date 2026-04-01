package com.sonictms.alsys.erp.erp105011.mapper;

import com.sonictms.alsys.erp.erp105011.entity.Erp105011VO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface Erp105011Mapper {

    // 로케이션별 재고 현황 조회
    List<Erp105011VO> selectLocationStockList(Erp105011VO vo);

    // 특정 로케이션의 재고 차감 (이동 출발지)
    int updateDecreaseStock(Erp105011VO vo);

    // 특정 로케이션의 재고 가산 (이동 목적지 - MERGE 문 권장)
    int upsertIncreaseStock(Erp105011VO vo);

    // 재고 이동 이력 저장 (History)
    int insertTransferHistory(Erp105011VO vo);

    int insertStockMoveLog(Erp105011VO vo);

    // 목적지 로케이션 유효성 확인 및 정보 조회
    Erp105011VO selectLocationInfoByNm(Erp105011VO vo);
}