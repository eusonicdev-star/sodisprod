package com.sonictms.alsys.erp.erp105001.service;

import com.sonictms.alsys.erp.erp105001.entity.Erp105001VO;
import com.sonictms.alsys.erp.erp105001.mapper.Erp105001Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp105001Service {

    private final Erp105001Mapper erp105001Mapper;

    // 재고 현황 리스트 조회
    public List<Erp105001VO> erp105001List(Erp105001VO erp105001VO) {
        List<Erp105001VO> list = erp105001Mapper.erp105001List(erp105001VO);
        return list;
    }

    // 재고 조정
    public boolean adjustInventory(Erp105001VO erp105001VO) {
        try {
            // 1. TBL_MTRL_M의 CURRENT_QUANTITY 업데이트
            int result = erp105001Mapper.adjustInventory(erp105001VO);
            
            if (result > 0) {
                // 2. TBL_INV_ADJUSTMENT에 이력 기록
                erp105001Mapper.insertAdjustmentHistory(erp105001VO);
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
