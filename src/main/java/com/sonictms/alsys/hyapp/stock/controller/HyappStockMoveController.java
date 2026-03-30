package com.sonictms.alsys.hyapp.stock.controller;

import com.sonictms.alsys.hyapp.stock.entity.HyappStockMoveVO;
import com.sonictms.alsys.hyapp.stock.service.HyappStockMoveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/hyapp/api/stock")
@RequiredArgsConstructor
public class HyappStockMoveController {

    private final HyappStockMoveService stockMoveService;

    // 1. 출발 로케이션의 재고 조회 (이전 질문의 loadStock 연동용)
    @GetMapping("/location/list")
    public Map<String, Object> getStockList(@RequestParam String cmpyCd, @RequestParam String locationCd) {
        Map<String, Object> result = new HashMap<>();
        result.put("items", stockMoveService.selectStockListByLocation(cmpyCd, locationCd));
        return result;
    }

    // 2. 재고 이동 실행 (saveMove 연동용)
    @PostMapping("/move")
    public Map<String, Object> moveStock(@RequestBody HyappStockMoveVO moveVO) {
        Map<String, Object> response = new HashMap<>();
        try {
            stockMoveService.processStockMove(moveVO);
            response.put("success", true);
            response.put("message", "재고 이동이 완료되었습니다.");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}