package com.allianceLogistics.alsys.erp.erp901006.controller;

import com.allianceLogistics.alsys.erp.erp901006.entity.Erp901006VO;
import com.allianceLogistics.alsys.erp.erp901006.service.Erp901006Service;
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
public class Erp901006Controller {

    private final Erp901006Service erp901006Service;

    // 화면 열기
    @GetMapping(value = {"erp901006"})
    public ModelAndView getErp901006(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp901006/erp901006");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp901006List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901006VO> erp901006List(Erp901006VO erp901006VO) {
        List<Erp901006VO> list = erp901006Service.erp901006List(erp901006VO);
        return list;
    }

    // 양식등록 팝업 화면 열기
    @PostMapping(value = {"erp901006p1"})
    public ModelAndView erp901006p1(ModelAndView modelAndView, @Valid Erp901006VO erp901006VO) {
        modelAndView.setViewName("erp/erp901006/erp901006p1");
        modelAndView.addObject("sendObject", erp901006VO);
        return modelAndView;
    }

    // 양식수정 팝업 화면 열기
    @PostMapping(value = {"erp901006p2"})
    public ModelAndView erp901006p2(ModelAndView modelAndView, @Valid Erp901006VO erp901006VO) {
        modelAndView.setViewName("erp/erp901006/erp901006p2");
        modelAndView.addObject("sendObject", erp901006VO);
        return modelAndView;
    }

    // 양식 저장하기
    @RequestMapping(value = {"erp901006p1Save"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp901006VO erp901006p1Save(@RequestBody Erp901006VO erp901006VO) {

        erp901006VO = erp901006Service.erp901006p1Save(erp901006VO);

        return erp901006VO;
    }

    // 수정팝업에서 내용 조회하기
    @RequestMapping(value = {"erp901006p2List"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp901006VO erp901006p2List(Erp901006VO erp901006VO) {
        erp901006VO = erp901006Service.erp901006p2List(erp901006VO);
        return erp901006VO;
    }

    // 양식 수정하기
    @RequestMapping(value = {"erp901006p2Updt"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp901006VO erp901006p2Updt(@RequestBody Erp901006VO erp901006VO) {

        erp901006VO = erp901006Service.erp901006p1Save(erp901006VO);

        return erp901006VO;
    }

}