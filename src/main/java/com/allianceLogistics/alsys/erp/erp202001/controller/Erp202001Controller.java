package com.allianceLogistics.alsys.erp.erp202001.controller;

import com.allianceLogistics.alsys.erp.erp202001.entity.Erp202001VO;
import com.allianceLogistics.alsys.erp.erp202001.service.Erp202001Service;
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
public class Erp202001Controller {

    private final Erp202001Service erp202001Service;

    // 시공좌석 생성/관리 화면 열기
    @GetMapping(value = {"erp202001"})
    public ModelAndView getErp202001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp202001/erp202001");

        return modelAndView;
    }

    // 좌성일괄생성 팝업창 열기
    @PostMapping(value = {"erp202001p1"})
    public ModelAndView erp202001p1(ModelAndView modelAndView, @Valid Erp202001VO erp202001VO) {
        modelAndView.setViewName("erp/erp202001/erp202001p1");
        modelAndView.addObject("sendObject", erp202001VO);
        return modelAndView;
    }

    // 좌석 점유 상세보기 팝업창 열기
    @PostMapping(value = {"erp202001p2"})
    public ModelAndView erp202001p2(ModelAndView modelAndView, @Valid Erp202001VO erp202001VO) {
        modelAndView.setViewName("erp/erp202001/erp202001p2");
        modelAndView.addObject("sendObject", erp202001VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp202001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp202001VO> erp202001List(Erp202001VO erp202001VO) {
        List<Erp202001VO> list = erp202001Service.erp202001List(erp202001VO);
        return list;
    }

    // erp202001p2 좌석 점유 상제 조회 리스트
    @RequestMapping(value = {"erp202001p2List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp202001VO> erp202001p2List(Erp202001VO erp202001VO) {
        List<Erp202001VO> list = erp202001Service.erp202001p2List(erp202001VO);
        return list;
    }

    // 시공좌석일괄생성 팝업창에서 일괄생성 저장하기
    @RequestMapping(value = {"erp202001p1Save"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp202001VO erp202001p1Save(Erp202001VO erp202001VO) {
        erp202001VO = erp202001Service.erp202001p1Save(erp202001VO);
        return erp202001VO;
    }

    // 체크수정 하기 버튼
    @RequestMapping(value = {"erp202001Save"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp202001VO> erp202001Save(@RequestBody List<Erp202001VO> erp202001VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다
        for (int i = 0; i < erp202001VO.size(); i++) {
            erp202001VO.set(i, erp202001Service.erp202001Save(erp202001VO.get(i)));

        }
        return erp202001VO;
    }

    // 좌석마감 하기
    @RequestMapping(value = {"erp202001End"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp202001VO> erp202001End(@RequestBody List<Erp202001VO> erp202001VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다
        for (int i = 0; i < erp202001VO.size(); i++) {
            erp202001VO.set(i, erp202001Service.erp202001End(erp202001VO.get(i)));

        }
        return erp202001VO;
    }


    // 20220118 정연호 시공좌석 조회 화면
    @GetMapping(value = {"erp202003"})
    public ModelAndView getErp202003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp202001/erp202003");

        return modelAndView;
    }

}