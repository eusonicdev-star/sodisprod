package com.sonictms.alsys.erp.erp901001.controller;

import com.sonictms.alsys.erp.erp901001.entity.Erp901001DetailVO;
import com.sonictms.alsys.erp.erp901001.entity.Erp901001VO;
import com.sonictms.alsys.erp.erp901001.service.Erp901001Service;
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
public class Erp901001Controller {

    private final Erp901001Service erp901001Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp901001"})
    public ModelAndView getErp901001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp901001/erp901001");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp901001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901001VO> erp901001List(Erp901001VO erp901001VO) {
        List<Erp901001VO> list = erp901001Service.erp901001List(erp901001VO);
        return list;
    }

    // 화주사 등록/수정 팝업
    @PostMapping(value = {"erp901001p1"})
    public ModelAndView erp901001p1(ModelAndView modelAndView, @Valid Erp901001VO erp901001VO) {
        modelAndView.setViewName("erp/erp901001/erp901001p1");
        modelAndView.addObject("sendObject", erp901001VO);
        return modelAndView;
    }

    // 고유 ID 로 정보 찾기
    @RequestMapping(value = {"erp901001p1agencySrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp901001VO erp901001p1agencySrch(Erp901001VO erp901001VO) {
        erp901001VO = erp901001Service.erp901001p1agencySrch(erp901001VO);
        return erp901001VO;
    }

    // 팝업 화주 등록/ 수정 하기
    @RequestMapping(value = {"erp901001p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp901001VO erp901001p1ComdSave(Erp901001VO erp901001VO) {
        erp901001VO = erp901001Service.erp901001p1ComdSave(erp901001VO);
        return erp901001VO;
    }

    //20220124 정연호 알림톡 연락처 팝업
    @PostMapping(value = {"erp901001p2"})
    public ModelAndView erp901001p2(ModelAndView modelAndView, @Valid Erp901001VO erp901001VO) {
        modelAndView.setViewName("erp/erp901001/erp901001p2");
        modelAndView.addObject("sendObject", erp901001VO);
        return modelAndView;
    }

    //20220124 정연호 알림톡 연락처용 화주사 저장/수정하기
    @RequestMapping(value = {"erp901001p2ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp901001VO erp901001p2ComdSave(@RequestBody Erp901001VO erp901001VO) {
        erp901001VO = erp901001Service.erp901001p2ComdSave(erp901001VO);
        return erp901001VO;
    }

    //20220124 정연호. 화주사 코드로 알림톡 연락처 조회하기
    @RequestMapping(value = {"erp901001p2alrmTlkSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901001DetailVO> erp901001p2alrmTlkSrch(Erp901001DetailVO erp901001DetailVO) {
        List<Erp901001DetailVO> list = erp901001Service.erp901001p2alrmTlkSrch(erp901001DetailVO);
        return list;
    }

}