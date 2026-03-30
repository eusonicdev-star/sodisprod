package com.sonictms.alsys.hyapp.common.controller;

import com.sonictms.alsys.hyapp.common.service.HyappCommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/common")
public class HyappCommonController {

    @Autowired
    private HyappCommonService hyappCommonService;

    /**
     * 물류센터 목록 조회 API
     * 호출 예: /api/common/dc-list?cmpyCd=A
     */
    @GetMapping("/dc-list")
    public List<Map<String, Object>> getDcList(@RequestParam String cmpyCd) {
        return hyappCommonService.getDcList(cmpyCd);
    }
}