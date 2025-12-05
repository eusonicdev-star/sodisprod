package com.sonictms.alsys.erp.erp105001.mapper;

import com.sonictms.alsys.erp.erp105001.entity.Erp105001VO;
import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105001Mapper {

    // 재고 현황 리스트 조회
    List<Erp105001VO> erp105001List(Erp105001VO erp105001VO);

    // 재고 조정
    int adjustInventory(Erp105001VO erp105001VO);

    // 재고 조정 이력 기록
    int insertAdjustmentHistory(Erp105001VO erp105001VO);

    // 출고 대기 주문 조회 (모달용 - 가용재고 계산과 동일한 로직)
    List<Erp105008VO> selectOutboundWaitListForModal(Erp105001VO erp105001VO);

}
