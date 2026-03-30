package com.sonictms.alsys.hyapp.print.controller;


import com.sonictms.alsys.hyapp.print.service.BarcodePrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/print") // 에이전트의 application.properties 설정과 맞춤
public class BarcodePrintRestController {

    @Autowired
    private BarcodePrintService barcodePrintService;

    /**
     * 1. 출력 대기 리스트 전달 (에이전트가 5초마다 호출)
     * GET /api/print/pending
     */
    @GetMapping("/request/list")
    public List<Map<String, Object>> getPendingList() {
        // BarcodePrintService에 이미 구현된 getPendingListAsMap 호출
        return barcodePrintService.getPendingListAsMap();
    }

    /**
     * 2. 출력 결과 처리 (에이전트가 출력 후 호출)
     * POST /api/print/execute

    @PostMapping("/execute")
    public String completePrint(@RequestBody Map<String, Object> params) {
        try {
            // 전달받은 SEQ로 상태 업데이트 및 출력 처리 로직 실행
            // (에이전트가 이미 출력을 직접 하므로, 서버에서는 상태만 DONE으로 바꿔도 됩니다)
            int seq = Integer.parseInt(params.get("SEQ").toString());
            barcodePrintService.executePrintBySeq(seq);
            return "SUCCESS";
        } catch (Exception e) {
            e.printStackTrace();
            return "FAIL: " + e.getMessage();
        }
    }
     */
}