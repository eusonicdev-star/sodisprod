package com.sonictms.alsys.wh.controller;

import com.sonictms.alsys.wh.entity.WhZoneVO;
import com.sonictms.alsys.wh.service.WhZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/wh/zone")
public class WhZoneController {

    @Autowired
    private WhZoneService zoneService;

    @PostMapping("/list")
    public ResponseEntity<?> getList(@RequestBody Map<String, Object> params) {
        return ResponseEntity.ok(zoneService.getZoneList(params));
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody WhZoneVO vo) {
        zoneService.saveZone(vo);
        return ResponseEntity.ok("저장되었습니다.");
    }
}