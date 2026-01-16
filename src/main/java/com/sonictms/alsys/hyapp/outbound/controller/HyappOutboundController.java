package com.sonictms.alsys.hyapp.outbound.controller;

import com.sonictms.alsys.hyapp.outbound.entity.HyappOutboundVO;
import com.sonictms.alsys.hyapp.outbound.mapper.HyappOutboundMapper;
import com.sonictms.alsys.hyapp.outbound.service.HyappOutboundService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hyapp/api")
@RequiredArgsConstructor
public class HyappOutboundController {

    private final HyappOutboundService outboundService;

    @GetMapping("/outbound/pick/list")
    public List<HyappOutboundVO> pickList(@RequestParam String cmpyCd,
                                          @RequestParam String dcCd,
                                          @RequestParam String fromDt,
                                          @RequestParam String toDt) {
        return outboundService.selectPickListByDlvyCnfmDt(cmpyCd, dcCd, fromDt, toDt);
    }
}

