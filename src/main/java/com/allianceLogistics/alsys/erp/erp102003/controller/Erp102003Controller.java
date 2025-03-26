package com.allianceLogistics.alsys.erp.erp102003.controller;

import com.allianceLogistics.alsys.erp.erp102003.entity.Erp102003VO;
import com.allianceLogistics.alsys.erp.erp102003.service.Erp102003Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp102003Controller {

    private final Erp102003Service erp102003Service;

    // 알림톡 발송 조회관리
    @GetMapping(value = {"erp102003"})
    public ModelAndView getErp102003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp102003/erp102003");
        return modelAndView;
    }

    // 알림톡 전송 팝업
    @PostMapping(value = {"erp102003p1"})
    public ModelAndView erp102003p1(ModelAndView modelAndView, @Valid Erp102003VO erp102003VO) {    //@Valid		//@RequestBody
        modelAndView.setViewName("erp/erp102003/erp102003p1");
        modelAndView.addObject("sendObject", erp102003VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp102003List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102003VO> erp102003List(Erp102003VO erp102003VO) {
        List<Erp102003VO> list = erp102003Service.erp102003List(erp102003VO);
        return list;
    }
}