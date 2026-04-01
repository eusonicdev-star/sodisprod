package com.sonictms.alsys.erp.erp105011.service;

import com.sonictms.alsys.erp.erp105011.entity.Erp105011VO;
import com.sonictms.alsys.erp.erp105011.mapper.Erp105011Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class Erp105011Service {

    private final Erp105011Mapper erp105011Mapper;

    public List<Erp105011VO> getLocationStockList(Erp105011VO vo) {
        return erp105011Mapper.selectLocationStockList(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> processStockTransfer(Erp105011VO vo) {
        Map<String, Object> result = new HashMap<>();

        // 1. 목적지 코드(toLocCd)로 ID 및 정보 조회 (유효성 체크)
        Erp105011VO toLocInfo = erp105011Mapper.selectLocationInfoByNm(vo);
        if (toLocInfo == null) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "목적지 로케이션이 존재하지 않거나 유효하지 않습니다.");
            return result;
        }

        // 2. 조회된 정보를 VO에 세팅 (toLocId, whCd, zoneId 등)
        vo.setToLocId(toLocInfo.getLocId());
        vo.setWhCd(toLocInfo.getWhCd());
        vo.setZoneId(toLocInfo.getZoneId());

        try {
            // 3. 출발지 재고 차감
            int decCnt = erp105011Mapper.updateDecreaseStock(vo);
            if (decCnt <= 0) throw new RuntimeException("출발지 재고가 부족합니다.");

            // 4. 목적지 재고 가산 (Merge)
            erp105011Mapper.upsertIncreaseStock(vo);

            // 5. 재고 이동 로그 저장 (TBL_STOCK_MOVE_LOG)
            erp105011Mapper.insertStockMoveLog(vo);

            result.put("rtnYn", "Y");
            result.put("rtnMsg", "재고 이동 및 이력 저장이 완료되었습니다.");
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return result;
    }
}