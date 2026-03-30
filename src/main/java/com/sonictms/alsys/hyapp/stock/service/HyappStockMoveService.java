package com.sonictms.alsys.hyapp.stock.service;

import com.sonictms.alsys.hyapp.stock.entity.HyappStockMoveVO;
import com.sonictms.alsys.hyapp.stock.mapper.HyappStockMoveMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HyappStockMoveService {

    private final HyappStockMoveMapper stockMoveMapper;

    public List<Map<String, Object>> selectStockListByLocation(String cmpyCd, String locationCd) {
        return stockMoveMapper.selectStockListByLocation(cmpyCd, locationCd);
    }

    @Transactional(rollbackFor = Exception.class)
    public void processStockMove(HyappStockMoveVO moveVO) throws Exception {
        // 1. 출발지 재고 확인 및 차감
        int minusResult = stockMoveMapper.updateDecreaseStock(moveVO);
        if (minusResult == 0) {
            throw new Exception("출발지 재고가 부족하거나 존재하지 않습니다.");
        }

        // 2. 목적지 재고 가산 (Merge 문 권장)
        stockMoveMapper.upsertIncreaseStock(moveVO);

        // 3. 이동 이력 저장 (TBL_STOCK_MOVE_LOG)
        stockMoveMapper.insertMoveLog(moveVO);
    }
}