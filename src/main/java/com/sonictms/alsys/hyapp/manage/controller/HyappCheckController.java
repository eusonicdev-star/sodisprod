package com.sonictms.alsys.hyapp.manage.controller;

import com.sonictms.alsys.hyapp.manage.service.HyappCheckService;
import org.springframework.stereotype.Controller; // View 반환을 위해 Controller 사용 권장
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hyapp/api")
public class HyappCheckController {

    private final HyappCheckService hyappCheckService;

    public HyappCheckController(HyappCheckService hyappCheckService) {
        this.hyappCheckService = hyappCheckService;
    }
    /**
     * 1) 전산 재고 조회
     * - 기존 쿼리(selectRackItemList) 로직을 활용하여
     * - 해당 로케이션에 있는 시스템상 재고 총량을 조회합니다.
     */
    @ResponseBody
    @GetMapping("/check/system-stock")
    public List<Map<String, Object>> getSystemStock(@RequestParam String locCd) {
        return hyappCheckService.getSystemStock(locCd);
    }

    // 2) 실사 저장 (확인/조정/저장 공통)
    @ResponseBody
    @PostMapping("/check/save")
    public void saveCheck(@RequestBody Map<String, Object> params) {
        hyappCheckService.saveCheck(params);
    }

    // [New] 최근 3개월 실사 일자별 요약 (날짜, 건수)
    @ResponseBody
    @GetMapping("/check/history/summary")
    public List<Map<String, Object>> getCheckHistorySummary() {
        return hyappCheckService.getCheckHistorySummary();
    }

    // 3) 실사 내역 조회 (현재 로케이션의 금일 작업분)
    @ResponseBody
    @GetMapping("/check/list")
    public List<Map<String, Object>> getCheckList(@RequestParam String locCd) {
        return hyappCheckService.getCheckList(locCd);
    }

    // 4) 상품 바코드 정보 조회 (스캔 시 명칭 표시용)
    @ResponseBody
    @GetMapping("/check/product")
    public Map<String, Object> getProductInfo(@RequestParam String barcode) {
        return hyappCheckService.getProductInfo(barcode);
    }

    // 5) 일자별 전체 실사 이력 조회
    @ResponseBody
    @GetMapping("/check/history")
    public List<Map<String, Object>> getCheckHistory(@RequestParam String date) {
        return hyappCheckService.getCheckHistory(date);
    }
}