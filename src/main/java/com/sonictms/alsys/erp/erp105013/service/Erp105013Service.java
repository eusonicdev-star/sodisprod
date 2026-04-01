package com.sonictms.alsys.erp.erp105013.service;

import com.sonictms.alsys.erp.erp105013.entity.Erp105013VO;
import com.sonictms.alsys.erp.erp105013.mapper.Erp105013Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp105013Service {

    private final Erp105013Mapper erp105013Mapper;

    @Transactional(rollbackFor = Exception.class)
    public void saveTempStock(List<Erp105013VO> list) {
        if (list == null || list.isEmpty()) return;

        // 첫 번째 항목의 정보를 기준으로 기존 임시 데이터 전체 삭제
        erp105013Mapper.deleteStockTemp(list.get(0));

        // 리스트를 순회하며 임시 테이블에 삽입
        for (Erp105013VO vo : list) {
            erp105013Mapper.insertStockTemp(vo);
        }
    }

    //임시 저장 테이블 불러오기
    public List<Erp105013VO> getTempStockList(Erp105013VO erp105013VO) {
        List<Erp105013VO> list = erp105013Mapper.getTempStockList(erp105013VO);
        return list;
    }

    @Transactional(rollbackFor = Exception.class)
    public int createMissingLocations(Erp105013VO vo) {
        // 1. 누락된 로케이션 목록 찾기 및 삽입 수행
        return erp105013Mapper.insertMissingLocations(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createFloorLocations(Erp105013VO vo) {
        // 세션 정보가 있다면 유저 ID 세팅
        if (vo.getSaveUser() == null) vo.setSaveUser("SYSTEM_AUTO");
        erp105013Mapper.insertFloorLocations(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void confirmFinalStock(Erp105013VO vo) {
        // 1. 히스토리 백업
        erp105013Mapper.insertStockHistory(vo);

        // 2. 기존 재고 삭제
        erp105013Mapper.deleteActualStock(vo);

        // 3. 신규 재고 삽입 (평치 로직 포함)
        erp105013Mapper.insertFinalStock(vo);

        // 4. [추가] 임시 테이블 상태 업데이트
        erp105013Mapper.updateTempStatus(vo);

        // 5. [추가] 상품 마스터(TBL_MTRL_M)의 전체 수량 업데이트 (최종)
        erp105013Mapper.updateMtrlMasterQuantity(vo);

        // 6. 최종 상태값 변경
        erp105013Mapper.updateTempStockStatus(vo);
    }
}