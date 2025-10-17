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
            // 재고 조정 로직 구현
            // 1. 현재 재고 확인
            // 2. 조정 유형에 따른 수량 계산
            // 3. 재고 업데이트
            // 4. 조정 이력 저장
            
            int result = erp105001Mapper.adjustInventory(erp105001VO);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
