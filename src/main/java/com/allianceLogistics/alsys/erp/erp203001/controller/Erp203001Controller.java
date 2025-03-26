package com.allianceLogistics.alsys.erp.erp203001.controller;

import com.allianceLogistics.alsys.erp.erp203001.entity.Erp203001VO;
import com.allianceLogistics.alsys.erp.erp203001.service.Erp203001Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp203001Controller {

    @Autowired
    private final Erp203001Service erp203001Service;

    // 화면오픈
    @GetMapping(value = {"erp203001"})
    public ModelAndView getErp203001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp203001/erp203001");

        return modelAndView;
    }

    // 뉴 화면오픈
    @GetMapping(value = {"erp2030010"})
    public ModelAndView erp2030010(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp203001/erp2030010");

        return modelAndView;
    }

    // 시공기사 찾기 팝업오픈
    @PostMapping(value = {"erp203001p1"})
    public ModelAndView erp203001p1(ModelAndView modelAndView, @Valid Erp203001VO erp203001VO) {
        modelAndView.setViewName("erp/erp203001/erp203001p1");
        modelAndView.addObject("sendObject", erp203001VO);
        return modelAndView;
    }

    // 시공기사 찾기 팝업오픈2
    @PostMapping(value = {"erp203001p2"})
    public ModelAndView erp203001p2(ModelAndView modelAndView, @Valid Erp203001VO erp203001VO) {
        modelAndView.setViewName("erp/erp203001/erp203001p2");
        modelAndView.addObject("sendObject", erp203001VO);
        return modelAndView;
    }

    // 시공기사 현황 팝업오픈3
    @PostMapping(value = {"erp203001p3"})
    public ModelAndView erp203001p3(ModelAndView modelAndView, @Valid Erp203001VO erp203001VO) {
        modelAndView.setViewName("erp/erp203001/erp203001p3");
        modelAndView.addObject("sendObject", erp203001VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp203001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203001VO> erp203001List(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Service.erp203001List(erp203001VO);
        return list;
    }

    // 리스트조회2 - 아래쪽 상세 그리드 조회
    @RequestMapping(value = {"erp203001List2"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203001VO> erp203001List2(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Service.erp203001List2(erp203001VO);
        return list;
    }

    // 시공기사 찾기 조회
    @RequestMapping(value = {"erp203001InstErSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203001VO> erp203001InstErSrch(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Service.erp203001InstErSrch(erp203001VO);
        return list;
    }

    // 시공기사 찾기 조회2. 팝업2에서 조회하기
    @RequestMapping(value = {"erp203001InstErSrch2"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203001VO> erp203001InstErSrch2(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Service.erp203001InstErSrch2(erp203001VO);
        return list;
    }

    // 시공기사 현황 3. 팝업3에서 조회하기
    @RequestMapping(value = {"erp203001InstErSrch3"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203001VO> erp203001InstErSrch3(Erp203001VO erp203001VO) {
        List<Erp203001VO> list = erp203001Service.erp203001InstErSrch3(erp203001VO);
        return list;
    }
    // 그리드에서 순번, 팔렛트, 시공기사 입력한것 저장 또는 수정 하기

    @RequestMapping(value = {"erp203001Save"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp203001VO> erp203001Save(@RequestBody List<Erp203001VO> erp203001VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다
        for (int i = 0; i < erp203001VO.size(); i++) {
            erp203001VO.set(i, erp203001Service.erp203001Save(erp203001VO.get(i)));
        }
        return erp203001VO;
    }

    // 체크한것들 삭제(일괄삭제)
    @RequestMapping(value = {"erp203001DelChk"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp203001VO> erp203001DelChk(@RequestBody List<Erp203001VO> erp203001VO) {
        for (int i = 0; i < erp203001VO.size(); i++) {
            String soNo = erp203001VO.get(i).getSoNo();
            erp203001VO.set(i, erp203001Service.erp203001DelChk(erp203001VO.get(i)));
            erp203001VO.get(i).setSoNo(soNo);
        }

        return erp203001VO;
    }

}