package com.allianceLogistics.alsys.erp.erp902005.controller;

import com.allianceLogistics.alsys.erp.erp902005.entity.Erp902005VO;
import com.allianceLogistics.alsys.erp.erp902005.service.Erp902005Service;
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
public class Erp902005Controller {

    private final Erp902005Service erp902005Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp902005"})
    public ModelAndView getErp902005(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp902005/erp902005");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp902005List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902005VO> erp902005List(Erp902005VO erp902005VO) {
        List<Erp902005VO> list = erp902005Service.erp902005List(erp902005VO);
        return list;
    }

    // 달력일괄생성 팝업창 열기
    @PostMapping(value = {"erp902005p1"})
    public ModelAndView erp902005p1(ModelAndView modelAndView, @Valid Erp902005VO erp902005VO) {
        modelAndView.setViewName("erp/erp902005/erp902005p1");
        modelAndView.addObject("sendObject", erp902005VO);
        return modelAndView;
    }

    // 일괄저장 하기 버튼
    @RequestMapping(value = {"erp902005Save"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902005VO> erp902005Save(@RequestBody List<Erp902005VO> erp902005VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다
        for (int i = 0; i < erp902005VO.size(); i++) {
            erp902005VO.set(i, erp902005Service.erp902005Save(erp902005VO.get(i)));

        }
        return erp902005VO;
    }

    // 달력일괄생성 팝업창에서 일괄생성 저장하기
    @RequestMapping(value = {"erp902005p1Save"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp902005VO erp902005p1Save(Erp902005VO erp902005VO) {
        erp902005VO = erp902005Service.erp902005p1Save(erp902005VO);
        return erp902005VO;
    }

}