package com.sonictms.alsys.hyapp.restock.service;

import com.sonictms.alsys.erp.erp105006.entity.Erp105006VO;
import com.sonictms.alsys.erp.erp105006.service.Erp105006Service;
import com.sonictms.alsys.hyapp.restock.entity.HyappRestockVO;
import com.sonictms.alsys.hyapp.restock.mapper.HyappRestockMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HyappRestockService {
    private final HyappRestockMapper hyappRestockMapper;
    private final Erp105006Service erp105006Service; // 기존 웹 서비스 재사용

    @Transactional
    public Map<String, Object> processRestock(HyappRestockVO vo) {
        Map<String, Object> result = new HashMap<>();
        String targetId = vo.getSelectedId();

        if (targetId == null || targetId.isEmpty()) {
            List<HyappRestockVO> targetItems = hyappRestockMapper.selectTargetItemsBySoNo(vo.getBarcode());
            if (targetItems.isEmpty()) {
                result.put("status", "FALSE");
                result.put("msg", "대기 중인 품목이 없습니다.");
                return result;
            }

            if (targetItems.size() > 1) {
                result.put("status", "SELECT_ITEM");
                result.put("itemList", targetItems);
                return result;
            } else {
                targetId = targetItems.get(0).getTblRestockWaitId();
            }
        }

        // [수정] 미사용 메서드였던 updateRestockComplete를 여기서 호출합니다.
        HyappRestockVO updateParam = new HyappRestockVO();
        updateParam.setTblRestockWaitId(targetId); // XML의 #{tblRestockWaitId}와 매핑
        updateParam.setSaveUser(vo.getUserName()); // XML의 #{saveUser}와 매핑

        int count = hyappRestockMapper.updateRestockComplete(updateParam);

        if (count > 0) {
            // 상태 변경 후, 기존 서비스의 재고 가산 로직만 별도로 수행 (필요 시)
            // 만약 erp105006BatchInboundComplete 내부에 복잡한 재고 계산 로직이 있다면
            // 그대로 두셔도 되지만, 단일 처리는 이 메서드가 담당하게 됩니다.
            result.put("status", "OK");
            result.put("count", 1);
        } else {
            result.put("status", "FALSE");
            result.put("msg", "업데이트에 실패했습니다.");
        }

        return result;
    }

    public List<HyappRestockVO> getPutawayWaitList(String cmpyCd, String dcCd) {
        return hyappRestockMapper.selectHyappRestockPutList(cmpyCd, dcCd);
    }

    @Transactional
    public void processPutawayIncrement(String waitId, String saveUser, String locBarcode) {
        // 1. 상태 업데이트 (PUTAWAY_YN = 'Y')
        int updated = hyappRestockMapper.updatePutawayStatus(waitId, saveUser);

        if (updated > 0) {
            // 2. 파라미터를 Map에 담아 전달 (waitId와 locBarcode를 함께 보냄)
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("waitId", waitId);
            paramMap.put("locBarcode", locBarcode);

            // 실제 재고/로케이션 수량 증가 처리
            hyappRestockMapper.updateLocationStockIncrement(paramMap);
        } else {
            throw new RuntimeException("이미 적치 완료되었거나 대상을 찾을 수 없습니다.");
        }
    }

    public List<HyappRestockVO> getReturnInboundWaitList(HyappRestockVO vo) {
        // 쿼리에서 날짜 형식이 yyyyMMdd일 경우 하이픈 제거 로직 필요 시 추가
        if(vo.getFromDt() != null) vo.setFromDt(vo.getFromDt().replace("-", ""));
        if(vo.getToDt() != null) vo.setToDt(vo.getToDt().replace("-", ""));

        return hyappRestockMapper.selectReturnInboundWaitList(vo);
    }
}