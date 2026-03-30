package com.sonictms.alsys.erp.erp105012.service;

import com.sonictms.alsys.erp.erp105012.entity.Erp105012VO;
import com.sonictms.alsys.erp.erp105012.mapper.Erp105012Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map; // 임포트 추가

@RequiredArgsConstructor
@Service
public class Erp105012Service {

    private final Erp105012Mapper erp105012Mapper;

    public List<Erp105012VO> getProductPickingRackList(Erp105012VO vo) {
        return erp105012Mapper.getProductPickingRackList(vo);
    }

    public List<Map<String, Object>> getStockLocationList(Erp105012VO vo) {
        return erp105012Mapper.getStockLocationList(vo);
    }

    public List<Map<String, Object>> getFullPickingLocationList(Erp105012VO vo) {
        // XML의 ID와 맞춤
        return erp105012Mapper.getAvailablePickingRackList(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void autoAssignPickingRack(Erp105012VO vo) {
        // 1:1 원칙을 준수하며 BIN_NO 기준 최적 위치 선정 및 승격 업데이트 실행
        erp105012Mapper.autoAssignPickingRack(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProductPickingRack(Erp105012VO vo) throws Exception {
        // 1. 사용자가 입력한 텍스트(locCd)로 실제 LOC_ID를 조회합니다.
        String foundLocId = erp105012Mapper.getLocIdByCode(vo);

        // 2. 입력한 코드가 시스템에 없는 경우 에러를 던집니다.
        if (foundLocId == null || foundLocId.isEmpty()) {
            throw new Exception("존재하지 않거나 사용 불가능한 로케이션 코드입니다: " + vo.getLocCd());
        }

        // 3. 조회된 ID를 VO에 세팅하여 업데이트를 진행합니다.
        vo.setPickingLocId(foundLocId);
        erp105012Mapper.updatePickingRackByCode(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateFixedLocFromFloor(Erp105012VO vo) {
        // 평치 로케이션 정보를 FIXED_LOC_CD에 반영하는 쿼리 호출
        erp105012Mapper.updateFixedLocFromFloor(vo);
    }
}