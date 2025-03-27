package com.sonictms.alsys.erp.erp902001.controller;

import com.sonictms.alsys.erp.erp902001.entity.Erp902001VO;
import com.sonictms.alsys.erp.erp902001.service.Erp902001Service;
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
public class Erp902001Controller {

    private final Erp902001Service erp902001Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp902001"})
    public ModelAndView getErp902001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp902001/erp902001");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp902001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902001VO> erp902001List(Erp902001VO erp902001VO) {
        List<Erp902001VO> list = erp902001Service.erp902001List(erp902001VO);
        return list;
    }

    // 지점등록 팝업
    @PostMapping(value = {"erp902001p1"})
    public ModelAndView erp902001p1(ModelAndView modelAndView, @Valid Erp902001VO erp902001VO) {
        modelAndView.setViewName("erp/erp902001/erp902001p1");
        modelAndView.addObject("sendObject", erp902001VO);
        return modelAndView;
    }

    // 지점 코드 중복 조회 하기
    @RequestMapping(value = {"erp902001p1CommDupl"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp902001VO erp902001p1CommDupl(Erp902001VO erp902001VO) {
        erp902001VO = erp902001Service.erp902001p1CommDupl(erp902001VO);
        return erp902001VO;
    }

    // 지점(물류센터)고유 ID 로 정보 찾기
    @RequestMapping(value = {"erp902001p1comdDcSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp902001VO erp902001p1comdDcSrch(Erp902001VO erp902001VO) {
        erp902001VO = erp902001Service.erp902001p1comdDcSrch(erp902001VO);
        return erp902001VO;
    }

    // 팝업 지점 등록/ 수정 하기
    @RequestMapping(value = {"erp902001p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp902001VO erp902001p1ComdSave(Erp902001VO erp902001VO) {
        erp902001VO = erp902001Service.erp902001p1ComdSave(erp902001VO);
        return erp902001VO;
    }

}