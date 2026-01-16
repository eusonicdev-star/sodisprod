package com.sonictms.alsys.hyapp.inbound.service;

import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.inbound.mapper.HyappInboundMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class HyappInboundService {

    private final HyappInboundMapper hyappInboundMapper;

    public HyappInboundService(HyappInboundMapper hyappInboundMapper) {
        this.hyappInboundMapper = hyappInboundMapper;
    }

    public List<HyappInboundVO> getInbound_barcode(HyappInboundVO vo) {
        return hyappInboundMapper.getHyappInboundList_barcode(vo);
    }

    /**
     * 검수 저장(TBL_INBOUND_PRODUCT 업데이트) + 재고 반영(TBL_MTRL_M 업데이트 & 이력 INSERT)
     * - updated == 0 이면 대상이 없거나, 이미 완료/조건 불일치로 업데이트가 안 된 상황
     */
    @Transactional
    public int saveInboundQcAndInventory(HyappInboundVO vo) {
        int updated = hyappInboundMapper.updateInboundQc(vo);

        if (updated > 0) {
            // 재고 반영에 필요한 필드(agntCd/mtrlCd/mtrlNm/tblMtrlMId 등)를 DB에서 보강
            HyappInboundVO forInv = hyappInboundMapper.selectInboundForInventory(vo);
            if (forInv != null) {
                // completeUser/actualDate/actualQuantity는 요청값 우선
                forInv.setCompleteUser(vo.getCompleteUser());
                forInv.setActualDate(vo.getActualDate());
                forInv.setActualQuantity(vo.getActualQuantity());

                hyappInboundMapper.updateMultipleInventoryQuantity(
                        java.util.Collections.singletonList(forInv)
                );

            }
        }
        return updated;
    }

    // 검수완료(COMPLETE) 목록 조회 - 적치 화면에서 사용
    public List<HyappInboundVO> getQcCompleteList(HyappInboundVO vo) {
        return hyappInboundMapper.selectQcCompleteList(vo);
    }

    // Zone List 조회
    public List<HyappInboundVO> getWhZoneList(HyappInboundVO vo) {
        return hyappInboundMapper.getWhZoneList(vo);
    }

    /**
     * 적치
     */
    @Transactional(rollbackFor = Exception.class)
    public int savePut(List<HyappInboundVO> list) {
        System.out.println("list - list: " + list.toString() );
        int processedCount = 0;
        for (HyappInboundVO vo : list) {
            // [체크] DB 필드 중 NOT NULL인데 데이터가 null로 들어오는지 확인
            System.out.println("DEBUG - cmpyCd: " + vo.getCmpyCd());
            System.out.println("DEBUG - AGNT_CD: " + vo.getAgntCd());
            System.out.println("DEBUG - locId: " + vo.getLocId());
            System.out.println("DEBUG - mtrlCd: " + vo.getMtrlCd());
            System.out.println("DEBUG - putawayQty: " + vo.getPutawayQty());
            System.out.println("DEBUG - WH_CD: " + vo.getWhCd());

            int updated = hyappInboundMapper.savePutOne(vo);
            System.out.println("DEBUG - savePutOne result: " + updated);

            if (updated > 0) {
                int upsert = hyappInboundMapper.upsertStock(vo);
                System.out.println("DEBUG - upsertStock result: " + upsert);
                processedCount++;
            }
        }
        return processedCount;
    }
}
