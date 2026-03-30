package com.sonictms.alsys.hyapp.outbound.service;

import com.sonictms.alsys.hyapp.info.entity.HyappNoticeVO;
import com.sonictms.alsys.hyapp.info.mapper.HyappNoticeMapper;
import com.sonictms.alsys.hyapp.outbound.entity.HyappOutboundVO;
import com.sonictms.alsys.hyapp.outbound.mapper.HyappOutboundMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HyappOutboundService {

    private final HyappOutboundMapper hyappOutboundMapper;
    private final HyappNoticeMapper hyappNoticeMapper;

    /**
     * 출고 검수 마스터 리스트 조회 (VO 기반)
     */
    public List<HyappOutboundVO> selectInspectionMasterList(HyappOutboundVO vo) {
        return hyappOutboundMapper.selectInspectionMasterList(vo);
    }

    /**
     * 출고 검수 상세 리스트 조회 (VO 기반)
     */
    public List<HyappOutboundVO> selectInspectionDetailList(HyappOutboundVO vo) {
        return hyappOutboundMapper.selectInspectionDetailList(vo);
    }

    /**
     * 바코드 스캔 처리 및 재고 관리 (VO 기반)
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> processScan(HyappOutboundVO vo) {
        Map<String, Object> result = new HashMap<>();
// 상세 로그 추가 (쿼리 파라미터 확인용)
        System.out.println("DEBUG: checkScanItem 호출 전 파라미터 확인");
        System.out.println("paltNoxx: " + vo.getPaltNoxx());
        System.out.println("barcode: " + vo.getBarcode());
        System.out.println("dlvyDt: " + vo.getDlvyDt()); // REPLACE 전 원본 확인
        System.out.println("cmpyCd: " + vo.getCmpyCd());
        // 로그 추가: 파라미터 유입 확인
        System.out.println("SCAN 파라미터 확인 -> PALT: " + vo.getPaltNoxx() + ", DT: " + vo.getDlvyDt() + ", STOCK_ID: " + vo.getSelectedStockId());

        // [Step 1] 스캔 대상 확인
        HyappOutboundVO item = hyappOutboundMapper.checkScanItem(vo);
        if (item == null) {
            // 여기서 에러가 난다면 paltNoxx 혹은 dlvyDt 값이 null이거나 형식이 틀린 것임
            result.put("status", "FALSE");
            result.put("resultMsg", "검수 대상 상품이 아니거나 스캔 정보가 부정확합니다.");
            return result;
        }

        // [Step 2] 차감할 재고 ID 결정
        if (vo.getSelectedStockId() != null) {
            // 팝업에서 선택하고 들어온 ID를 최우선으로 사용
            vo.setStockId(vo.getSelectedStockId());
        } else {
            // 자동 조회 로직
            vo.setMtrlCd(item.getMtrlCd());
            vo.setAgntCd(item.getAgntCd());
            List<HyappOutboundVO> availableStocks = hyappOutboundMapper.selectAllAvailableStocks(vo);

            if (availableStocks == null || availableStocks.isEmpty()) {
                result.put("status", "FALSE");
                result.put("resultMsg", "피킹랙에 가용 재고가 없습니다.");
                return result;
            }
            vo.setStockId(availableStocks.get(0).getStockId());
        }

        // [Step 3] 재고 차감 및 검수량 업데이트
        hyappOutboundMapper.updateStockDecrementById(vo);
        hyappOutboundMapper.updateScanQty(item.getTblSoPId());

        // [Step 3] 피킹랙 잔량 확인
        int currentStock = hyappOutboundMapper.getRemainingStock(vo.getStockId());

        // [Step 4] 재고가 0이 되었을 때 보충 로직 실행
        if (currentStock <= 0) {
            // 보관랙(PICKING_YN = 'N') 중 재고가 있는 로케이션 조회
            List<HyappOutboundVO> storageStocks = hyappOutboundMapper.selectStorageStocks(vo);

            if (storageStocks != null && !storageStocks.isEmpty()) {
                if (storageStocks.size() > 1) {
                    // 1) 로케이션이 2개 이상이면 선택 팝업 유도
                    result.put("status", "SELECT_REPLENISH_SOURCE");
                    result.put("storageStocks", storageStocks);
                    result.put("targetStockId", vo.getStockId()); // 보충될 피킹랙 ID
                    result.put("resultMsg", "피킹랙 재고 소진! 보충할 보관랙을 선택해주세요.");
                    return result;
                } else {
                    // 2) 로케이션이 1개이면 바로 이동 처리
                    HyappOutboundVO source = storageStocks.get(0);

                    // 이동 정보 세팅
                    vo.setFromStockId(source.getStockId()); // 가져올 곳 (보관랙)
                    vo.setMoveQty(source.getStockQty());    // 보관랙의 전체 수량 (팔렛트 단위)

                    // [해결] 목적지(피킹랙)의 로케이션 정보 확보
                    // checkScanItem에 정보가 없으므로, 현재 stockId로 위치 정보를 조회해와야 함
                    HyappOutboundVO targetLocInfo = hyappOutboundMapper.getStockLocationInfo(vo.getStockId());
                    if (targetLocInfo != null) {
                        vo.setFromLocNm(source.getLocNm()); // 보관랙 이름 세팅
                        vo.setWhCd(targetLocInfo.getWhCd());
                        vo.setZoneId(targetLocInfo.getZoneId());
                        vo.setLocId(targetLocInfo.getLocId());

                        vo.setFromLocNm(source.getLocNm());
                        vo.setToLocNm(targetLocInfo.getLocNm());
                        vo.setLocNm(targetLocInfo.getLocNm());

                        executeReplenishment(vo);

                        // 화면 알림용 데이터 추가
                        result.put("status", "REPLENISH_SUCCESS");

                        // Java 8 호환 방식: HashMap 사용
                        Map<String, Object> replenishInfo = new HashMap<>();
                        replenishInfo.put("fromLoc", source.getLocNm());
                        replenishInfo.put("toLoc", targetLocInfo.getLocNm());
                        replenishInfo.put("qty", vo.getMoveQty());
                        replenishInfo.put("mtrlNm", item.getMtrlNm());

                        result.put("replenishInfo", replenishInfo);
                        result.put("resultMsg", "재고가 자동 보충되었습니다.");

                        return result; // [핵심] 보충 성공 시 여기서 즉시 반환하여 'OK'로 덮어쓰기 방지
                    }
                }
            }
        }

        result.put("status", "OK");
        return result;
    }

    /**
     * 실제 재고 이동 처리 (보관랙 -> 피킹랙)
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> processReplenishment(HyappOutboundVO vo) {
        // 1. 선택된 보관랙(From)의 마스터 정보 조회 (agntCd, mtrlCd 및 로케이션명 확보)
        HyappOutboundVO sourceInfo = hyappOutboundMapper.getStockLocationInfo(vo.getFromStockId());

        if (sourceInfo != null) {
            vo.setAgntCd(sourceInfo.getAgntCd());
            vo.setMtrlCd(sourceInfo.getMtrlCd());
            vo.setFromLocNm(sourceInfo.getLocNm()); // [추가] 보관랙 명칭 세팅

            // 수량 방어 코드
            if (vo.getMoveQty() == null || vo.getMoveQty() <= 0) {
                vo.setMoveQty(sourceInfo.getStockQty());
            }

            // 2. 목적지(피킹랙) 위치 정보 조회 (LOC_NM 포함 상세 정보 확보)
            HyappOutboundVO targetInfo = hyappOutboundMapper.getStockLocationInfo(vo.getToStockId());
            if(targetInfo != null) {
                vo.setLocId(targetInfo.getLocId());
                vo.setWhCd(targetInfo.getWhCd());
                vo.setZoneId(targetInfo.getZoneId());
                vo.setLocNm(targetInfo.getLocNm()); // [추가] 피킹랙 명칭 세팅
            }
        }

        // 3. 실제 재고 이동 실행 및 알림 저장
        executeReplenishment(vo);

        Map<String, Object> result = new HashMap<>();
        result.put("status", "REPLENISH_SUCCESS");

        // [수정] 알림용 정보 구성 (ID 대신 명칭 사용)
        Map<String, Object> replenishInfo = new HashMap<>();
        // sourceInfo가 null이 아닐 때 locNm을 우선 사용, 없으면 locId 사용
        replenishInfo.put("fromLoc", (sourceInfo != null && sourceInfo.getLocNm() != null) ? sourceInfo.getLocNm() : "보관랙");
        replenishInfo.put("toLoc", (vo.getLocNm() != null) ? vo.getLocNm() : vo.getLocId());
        replenishInfo.put("qty", vo.getMoveQty());

        result.put("replenishInfo", replenishInfo);
        result.put("resultMsg", "재고 보충 완료");

        return result;
    }

    private void executeReplenishment(HyappOutboundVO vo) {
        // 1. 보관랙 재고 차감 (From) -> moveQty만큼 차감하여 0으로 만듦
        HyappOutboundVO fromVo = new HyappOutboundVO();
        fromVo.setStockId(vo.getFromStockId());
        fromVo.setMoveQty(vo.getMoveQty());
        fromVo.setUserId(vo.getUserId());
        hyappOutboundMapper.updateStockDecrementByQty(fromVo);

        // 2. 피킹랙 재고 가산 (To) -> upsertStock은 MERGE 문이므로 기존 위치에 합산됨
        // vo에는 위에서 세팅한 피킹랙의 WH_CD, ZONE_ID, LOC_ID가 들어있어야 함
        hyappOutboundMapper.upsertStock(vo);

        // 3. 알림 메시지 생성 및 저장
        HyappNoticeVO notice = new HyappNoticeVO();
        String fromLocNm = (vo.getFromLocNm() != null) ? vo.getFromLocNm() : "보관랙";
        String toLocNm = (vo.getLocNm() != null) ? vo.getLocNm() : vo.getLocId();

        String title = "재고 보충 알림";
        String content = String.format("[%s] 상품이 %s에서 %s(으)로 %d개 이동되었습니다. 실물을 확인해 주세요.",
                vo.getMtrlCd(), fromLocNm, toLocNm, vo.getMoveQty());

        notice.setCmpyCd(vo.getCmpyCd());
        notice.setNoticeType("REPLENISH");
        notice.setTitle(title);
        notice.setContent(content);
        notice.setSaveUser(vo.getUserId());

        hyappNoticeMapper.insertNotice(notice);
    }

    /**
     * 출고 검수 마스터 리스트 조회 (VO 기반)
     */
    public List<HyappOutboundVO> getOutCancleList(HyappOutboundVO vo) {
        return hyappOutboundMapper.getOutCancleList(vo);
    }

    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> confirmReturnProcess(HyappOutboundVO vo) {
        Map<String, Object> result = new HashMap<>();

        // 1. TBL_SO_P 상태 업데이트 ('Y' 처리)
        hyappOutboundMapper.updateReturnConfirm(vo);

        // 2. 스캔 수량이 있으면 재고 증가
        if (vo.getScanQty() != null && vo.getScanQty() > 0) {
            vo.setMoveQty(vo.getScanQty()); // 증가시킬 수량 설정
            hyappOutboundMapper.upsertStock(vo); // 기존 MERGE 쿼리 활용

            // 3. 마스터 테이블 수량 동기화
            hyappOutboundMapper.updateMaterialMasterStock(vo);
        }

        result.put("status", "OK");
        return result;
    }
}