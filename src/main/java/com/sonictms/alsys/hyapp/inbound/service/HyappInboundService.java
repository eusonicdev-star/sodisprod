package com.sonictms.alsys.hyapp.inbound.service;

import com.sonictms.alsys.hyapp.inbound.dto.InboundProductSearchDTO;
import com.sonictms.alsys.hyapp.inbound.dto.ProductSearchReq;
import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.inbound.mapper.HyappInboundMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class HyappInboundService {

    private final HyappInboundMapper hyappInboundMapper;

    public HyappInboundService(HyappInboundMapper hyappInboundMapper) {
        this.hyappInboundMapper = hyappInboundMapper;
    }

    public List<HyappInboundVO> getPendingInboundList(HyappInboundVO vo) {
        return hyappInboundMapper.selectPendingInboundList(vo);
    }

    public List<HyappInboundVO> getInbound_barcode(HyappInboundVO vo) {
        return hyappInboundMapper.getHyappInboundList_barcode(vo);
    }

    /**
     * 검수 저장 + (고정 로케이션 시) 자동 적치 프로세스
     */
    @Transactional(rollbackFor = Exception.class)
    public int saveInboundQcAndInventory(HyappInboundVO vo) {
        // 1. 검수 결과 저장 (실시간 ACTUAL_QUANTITY 누적 합산)
        int updated = hyappInboundMapper.updateInboundProductQc(vo);

        if (updated > 0) {
            // 이번에 입력된 합격 수량이 있을 때만 재고/적치 프로세스 실행
            if (vo.getActualQuantity() != null && vo.getActualQuantity() > 0) {

                // 재고 반영에 필요한 기본 정보(agntCd 등) 보강
                HyappInboundVO forInv = hyappInboundMapper.selectInboundForInventory(vo);


                if (forInv != null) {
                    // 고정 로케이션 정보 조회 (MTRL_CD 기준)
                    HyappInboundVO fixedInfo = hyappInboundMapper.getFixedLocationInfo(forInv.getMtrlCd(), vo.getCmpyCd());
/**
                    // [Case 1] 고정 로케이션이 있는 경우 -> 자동 적치 수행
                    if (fixedInfo != null && fixedInfo.getLocId() != null) {
                        // ★ 핵심: 이번에 검수된 '분할 수량' 만큼만 적치 이력 및 재고 반영
                        vo.setMtrlCd(forInv.getMtrlCd());
                        processAutoPutaway(vo, fixedInfo, vo.getActualQuantity(), forInv.getAgntCd());
                    }
**/
                    // 3. TBL_MTRL_M 재고 업데이트 및 이력 생성 실행
                    // Mapper가 List를 받으므로 Collections.singletonList로 전달합니다.
                    forInv.setActualQuantity(vo.getActualQuantity()); // 쿼리의 + NULL 해결
                    forInv.setCompleteUser(vo.getCompleteUser());     // UPDT_USER NULL 해결
                    forInv.setActualDate(vo.getActualDate());         // 이력 날짜 NULL 해결
                    forInv.setCmpyCd(vo.getCmpyCd());                 // WHERE 조건용
                    hyappInboundMapper.updateMultipleInventoryQuantity(Collections.singletonList(forInv));

                    // [불량 처리] 불량 수량은 별도로 폐기존 적치 (필요 시 유지)
                    if (vo.getFailQty() != null && vo.getFailQty() > 0) {
                        HyappInboundVO scrapInfo = hyappInboundMapper.getScrapLocationInfo(vo.getCmpyCd());
                        if (scrapInfo != null && scrapInfo.getLocId() != null) {
                            processAutoPutaway(vo, scrapInfo, vo.getFailQty(), forInv.getAgntCd());
                        }
                    }
                }

            }
        }
        return updated;
    }

    /**
     * 자동 적치 공통 프로세스 (분할 적치 지원)
     */
    private void processAutoPutaway(HyappInboundVO baseVo, HyappInboundVO locInfo, Integer qty, String agntCd) {
        HyappInboundVO putVo = new HyappInboundVO();
        putVo.setCmpyCd(baseVo.getCmpyCd());
        putVo.setTblInboundProductId(baseVo.getTblInboundProductId());
        putVo.setMtrlCd(baseVo.getMtrlCd());
        putVo.setWhCd(locInfo.getWhCd());
        putVo.setZoneId(locInfo.getZoneId());
        putVo.setLocId(locInfo.getLocId());
        putVo.setPutawayQty(qty); // 이번 회차 검수 수량
        putVo.setPutawayUser(baseVo.getCompleteUser());
        putVo.setAgntCd(agntCd);

        // 1. TBL_WH_PUT_H (적치 이력) Insert
        // 이 데이터가 쌓여야 전체 검수량 - 적치량 = 0 이 되어 목록에서 사라집니다.
        hyappInboundMapper.savePutOne(putVo);

        // 2. TBL_STOCK (실제 재고) Merge
        // 기존에 상품이 있으면 수량 증가, 없으면 신규 Insert 수행
        hyappInboundMapper.upsertStock(putVo);

        // ★ [삭제] updatePutawayStatusY(baseVo.getTblInboundProductId(), baseVo.getCmpyCd());
        // 무조건 종결시키지 않고, 쿼리(remainingQty)가 0이 될 때까지 리스트에 노출되도록 둡니다.
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

    /**
     * 출력 대기열에 데이터 추가
     */
    @Transactional
    public void insertPrintQueue(Map<String, Object> params) {
        hyappInboundMapper.insertPrintQueue(params);
    }

    /**
     * 신규 등록을 위한 상품 검색 (공백 분리 AND 검색 적용)
     */
    public List<InboundProductSearchDTO> findInboundProducts(String keyword, String cmpyCd) {
        ProductSearchReq req = new ProductSearchReq();
        req.setCmpyCd(cmpyCd);

        if (keyword != null && !keyword.trim().isEmpty()) {
            // Arrays.asList 결과를 new ArrayList로 한 번 더 감싸서 표준 객체로 변환 (중요!)
            List<String> list = new ArrayList<>(Arrays.asList(keyword.trim().split("\\s+")));
            req.setKeywordList(list);
        }

        return hyappInboundMapper.selectInboundProductList(req);
    }

    /**
     * 입고 예정 정보 신규 저장
     */
    @Transactional
    public int registerInboundProduct(HyappInboundVO vo) {
        // 기본 상태값 설정 (10: 예정)
        if (vo.getInboundStatus() == null) vo.setInboundStatus("10");
        return hyappInboundMapper.insertInboundProduct(vo);
    }

    /**
     * 입차 차량 정보(Master) 저장
     * @param vo 차량 정보 (vhclTonType, inMethod, palletQty 등)
     * @return 저장된 행의 수
     */
    @Transactional(rollbackFor = Exception.class)
    public int insertInboundMaster(HyappInboundVO vo) {
        // 1. CMPY_CD가 비어있을 경우 기본값 'A' 설정
        if (vo.getCmpyCd() == null || vo.getCmpyCd().trim().isEmpty()) {
            vo.setCmpyCd("A");
        }

        // 3. Mapper 호출 (PK는 vo 객체에 자동 생성되어 담김)
        return hyappInboundMapper.insertInboundMaster(vo);
    }

    /**
     * 입차 차량 마스터 리스트 조회
     * @param vo (cmpyCd, agntCd, actualDate 필드 포함)
     * @return 오늘 등록된 차량 목록
     */
    public List<HyappInboundVO> getInboundMasterList(HyappInboundVO vo) {
        // 1. 필수 파라미터 체크 (회사코드가 없을 경우 기본값 'A' 설정)
        if (vo.getCmpyCd() == null || vo.getCmpyCd().isEmpty()) {
            vo.setCmpyCd("A");
        }

        // 2. 매퍼 호출 및 결과 반환
        return hyappInboundMapper.selectInboundMasterList(vo);
    }

    public List<HyappInboundVO> getPutawayItemByBarcode(HyappInboundVO vo) {
        return hyappInboundMapper.selectPutawayItemByBarcode(vo);
    }
}
