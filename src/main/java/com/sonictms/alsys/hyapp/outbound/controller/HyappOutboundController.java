package com.sonictms.alsys.hyapp.outbound.controller;

import com.sonictms.alsys.hyapp.outbound.entity.HyappOutboundVO;
import com.sonictms.alsys.hyapp.outbound.service.HyappOutboundService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/hyapp/api")
@RequiredArgsConstructor
public class HyappOutboundController {

    private final HyappOutboundService hyappOutboundService;

    /**
     * 출고 검수 마스터 리스트 조회
     * GET 방식에서는 VO 객체로 선언하면 쿼리 스트링(?cmpyCd=A&...)이 필드에 자동 매핑됩니다.
     */
    @GetMapping("/outbound/master-list")
    public List<HyappOutboundVO> getMasterList(HyappOutboundVO vo) {
        // 별도의 Map 생성 없이 vo를 서비스로 바로 전달
        return hyappOutboundService.selectInspectionMasterList(vo);
    }

    /**
     * 출고 검수 상세 리스트 조회
     */
    @GetMapping("/outbound/detail-list")
    public List<HyappOutboundVO> getDetailList(HyappOutboundVO vo) {
        // paltNoxx, dlvyDt, cmpyCd 등이 vo의 필드에 자동으로 채워집니다.
        return hyappOutboundService.selectInspectionDetailList(vo);
    }

    /**
     * 바코드 스캔 처리
     * POST 방식이므로 @RequestBody를 사용하여 JSON 데이터를 VO로 받습니다.
     */
    @PostMapping("/outbound/scan")
    public Map<String, Object> processScan(@RequestBody HyappOutboundVO vo) {
        // paltNoxx, barcode, dlvyDt, selectedStockId 등을 VO로 한 번에 전달
        return hyappOutboundService.processScan(vo);
    }

    /**
     * 재고 보충 처리
     */
    @PostMapping("/outbound/replenish")
    public Map<String, Object> processReplenish(@RequestBody HyappOutboundVO vo) {
        // fromStockId, toStockId, moveQty 등을 VO에 담아 전달
        return hyappOutboundService.processReplenishment(vo);
    }

    /**
     * 출고 검수 상세 리스트 조회
     */
    @GetMapping("/outbound/cancel-list")
    public List<HyappOutboundVO> getOutCancleList(HyappOutboundVO vo) {
        // paltNoxx, dlvyDt, cmpyCd 등이 vo의 필드에 자동으로 채워집니다.
        return hyappOutboundService.getOutCancleList(vo);
    }

    /**
     * 출고 취소 상품 - 실물 재입고 확인 및 재고 원복 처리
     */
    @PostMapping("/outbound/confirm-return")
    public Map<String, Object> confirmReturn(@RequestBody HyappOutboundVO vo, HttpSession session) {
        // 1. 세션에서 현재 작업자 ID 추출
        String userId = (String) session.getAttribute("userId");
        if (userId == null) userId = (String) session.getAttribute("loginId");
        if (userId == null) userId = "SYSTEM"; // 방어 코드

        // 2. VO에 필수 정보 세팅
        vo.setUserId(userId);
        // cmpyCd가 프론트에서 안 넘어올 경우를 대비해 세션값 활용
        if (vo.getCmpyCd() == null) {
            vo.setCmpyCd((String) session.getAttribute("cmpyCd"));
        }

        // 3. 서비스 호출 (TBL_SO_P 업데이트 + 재고 가산 + 마스터 동기화 일괄 처리)
        // 서비스에 confirmReturnProcess 메서드가 정의되어 있어야 합니다.
        return hyappOutboundService.confirmReturnProcess(vo);
    }
}