package com.sonictms.alsys.hyapp.print.controller;

import com.sonictms.alsys.hyapp.print.service.BarcodePrintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/print")
public class BarcodePrintController {
    @Autowired private BarcodePrintService service;

    @GetMapping("/pending")
    public List<Map<String, Object>> getPending() {
        return service.getPendingListAsMap();
    }

    @PostMapping("/execute")
    public ResponseEntity<String> execute(@RequestBody Map<String, Object> params) {
        try {
            service.executePrintBySeq((int)params.get("SEQ"));
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/complete")
    public ResponseEntity<String> complete(@RequestBody Map<String, Object> params) {
        try {
            int seq = (int) params.get("SEQ");
            // DB 상태를 '1'(완료)로 업데이트
            service.updateStatus(seq);
            return ResponseEntity.ok("OK");
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}