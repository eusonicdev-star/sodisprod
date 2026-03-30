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

        try {
            // 1. 목적지 로케이션 정보 확인
            Erp105011VO toLocInfo = erp105011Mapper.selectLocationInfoByNm(vo);
            if (toLocInfo == null) {
                result.put("rtnYn", "N");
                result.put("rtnMsg", "목적지 로케이션이 유효하지 않습니다.");
                return result;
            }
            // 2. 중요: 조회된 결과(toLocId 등)를 원본 VO에 세팅하여 이후 쿼리에서 사용하게 함
            vo.setToLocId(toLocInfo.getLocId()); // 목적지 ID 세팅
            vo.setWhCd(toLocInfo.getWhCd());
            vo.setZoneId(toLocInfo.getZoneId());
            System.out.println(vo.getAgntCd());
            System.out.println(vo.getFromLocId());

            // 3. 출발지 재고 차감 실행
            // 이때 vo.getFromLocId()와 vo.getAgntCd()가 1번 과정(JS)에서 넘어온 상태여야 함
            int decCnt = erp105011Mapper.updateDecreaseStock(vo);

            if (decCnt <= 0) {
                throw new RuntimeException("출발지 재고가 부족하거나 정보가 올바르지 않습니다.");
            }

            // 4. 목적지 재고 가산 (vo.toLocId가 사용됨)
            erp105011Mapper.upsertIncreaseStock(vo);

            // 4. 재고 이동 이력 기록
            erp105011Mapper.insertTransferHistory(vo);

            result.put("rtnYn", "Y");
            result.put("rtnMsg", "재고 이동이 완료되었습니다.");

        } catch (Exception e) {
            log.error("재고 이동 처리 중 오류", e);
            result.put("rtnYn", "N");
            result.put("rtnMsg", "오류 발생: " + e.getMessage());
            throw new RuntimeException(e.getMessage()); // 트랜잭션 롤백
        }

        return result;
    }
}