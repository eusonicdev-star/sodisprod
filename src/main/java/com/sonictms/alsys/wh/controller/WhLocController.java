package com.sonictms.alsys.wh.controller;

import com.sonictms.alsys.wh.entity.WhLocVO;
import com.sonictms.alsys.wh.service.WhLocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/wh/loc")
public class WhLocController {
    @Autowired
    private WhLocService locService;

    @PostMapping("/list")
    public List<WhLocVO> getList(@RequestBody Map<String, Object> params) {
        return locService.getLocList(params);
    }

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody WhLocVO vo) {
        locService.saveLocation(vo);
        return ResponseEntity.ok("저장되었습니다.");
    }
}