package com.sonictms.alsys.wh.controller;

import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import com.sonictms.alsys.hyapp.inbound.service.HyappInboundService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Autowired
    private HyappInboundService hyappInboundService;

    // ✅ 여러 화면에서 공통으로 사용할 존 목록 API
    @GetMapping("/wh/zone/list")
    public List<HyappInboundVO> getWhZoneList(HttpSession session) {
        String cmpyCd = (String) session.getAttribute("cmpyCd");
        if (cmpyCd == null) cmpyCd = "A"; // 세션 없을 시 기본값 예외처리

        HyappInboundVO vo = new HyappInboundVO();
        vo.setCmpyCd(cmpyCd);
        vo.setWhCd("1000"); // 현재 센터 기준

        return hyappInboundService.getWhZoneList(vo);
    }
}