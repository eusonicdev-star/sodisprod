package com.allianceLogistics.alsys.erp.erp902003.controller;

import com.allianceLogistics.alsys.erp.erp902003.entity.Erp902003VO;
import com.allianceLogistics.alsys.erp.erp902003.service.Erp902003Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp902003Controller {

    private final Erp902003Service erp902003Service;

    //공통코드관리 화면 열기
    @GetMapping(value = {"erp902003"})
    public ModelAndView getErp902003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp902003/erp902003");

        return modelAndView;
    }


    //리스트조회
    @RequestMapping(value = {"erp902003List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902003VO> erp902003List(Erp902003VO erp902003VO) {
        List<Erp902003VO> list = erp902003Service.erp902003List(erp902003VO);
        return list;
    }


    //권역매핑저장하기

    @RequestMapping(value = {"erp902003Save"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902003VO> erp902003Save(@RequestBody List<Erp902003VO> erp902003VO) {    //화면에서 데이터를 json 형태로 보내는데 그게 어레이 일떄는 @RequestBody 를 붙여 받는다


        for (int i = 0; i < erp902003VO.size(); i++) {
            erp902003VO.set(i, erp902003Service.erp902003Save(erp902003VO.get(i)));

        }
        return erp902003VO;
    }

    //권역매핑삭제하기

    @RequestMapping(value = {"erp902003Delete"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902003VO> erp902003Delete(@RequestBody List<Erp902003VO> erp902003VO) {    //화면에서 데이터를 json 형태로 보내는데 그게 어레이 일떄는 @RequestBody 를 붙여 받는다


        for (int i = 0; i < erp902003VO.size(); i++) {
            erp902003VO.set(i, erp902003Service.erp902003Delete(erp902003VO.get(i)));
        }

        return erp902003VO;
    }


    //권역 등록 수정 팝업
    @PostMapping(value = {"erp902003p1"})
    public ModelAndView erp902003p1(ModelAndView modelAndView, @Valid Erp902003VO erp902003VO) {
        modelAndView.setViewName("erp/erp902003/erp902003p1");
        modelAndView.addObject("sendObject", erp902003VO);
        return modelAndView;
    }

    //권역 등록 수정 팝업의 중복확인하기
    @RequestMapping(value = {"erp902003p1DoubleChk"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp902003VO erp902003p1DoubleChk(Erp902003VO erp902003VO) {
        erp902003VO = erp902003Service.erp902003p1DoubleChk(erp902003VO);
        return erp902003VO;
    }


    //권역 등록 수정 팝업의 등록/수정 하기
    @RequestMapping(value = {"erp902003p1Save"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp902003VO erp902003p1Save(Erp902003VO erp902003VO) {
        erp902003VO = erp902003Service.erp902003p1Save(erp902003VO);
        return erp902003VO;
    }

} 