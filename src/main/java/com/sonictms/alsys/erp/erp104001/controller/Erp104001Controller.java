package com.sonictms.alsys.erp.erp104001.controller;

import com.sonictms.alsys.erp.erp104001.entity.Erp104001VO;
import com.sonictms.alsys.erp.erp104001.service.Erp104001Service;
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
public class Erp104001Controller {

    private final Erp104001Service erp104001Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp104001"})
    public ModelAndView getErp104001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp104001/erp104001");

        return modelAndView;
    }

    // 공통코드관리 화면 열기
    @PostMapping(value = {"erp104001p1"})
    public ModelAndView erp104001p1(ModelAndView modelAndView, @Valid Erp104001VO erp104001VO) {
        modelAndView.setViewName("erp/erp104001/erp104001p1");
        modelAndView.addObject("sendObject", erp104001VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp104001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp104001VO> erp104001List(Erp104001VO erp104001VO) {
        List<Erp104001VO> list = erp104001Service.erp104001List(erp104001VO);
        return list;
    }
}