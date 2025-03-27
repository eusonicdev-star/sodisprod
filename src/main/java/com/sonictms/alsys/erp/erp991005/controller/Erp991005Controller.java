package com.sonictms.alsys.erp.erp991005.controller;

import com.sonictms.alsys.erp.erp991005.entity.Erp991005VO;
import com.sonictms.alsys.erp.erp991005.service.Erp991005Service;
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
public class Erp991005Controller {

    private final Erp991005Service erp991005Service;

    //공통코드관리 화면 열기
    @GetMapping(value = {"erp991005"})
    public ModelAndView getErp991005(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp991005/erp991005");

        return modelAndView;
    }


    //왼쪽 마스터 공통목록 열기
    @RequestMapping(value = {"erp991005LCommList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp991005VO> erp991005LCommList(Erp991005VO erp991003VO) {
        List<Erp991005VO> list = erp991005Service.erp991005LCommList(erp991003VO);
        return list;
    }

    //오른쪽 상세 공통목록 열기
    @RequestMapping(value = {"erp991005RComdList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp991005VO> erp991005RComdList(Erp991005VO erp991003VO) {
        List<Erp991005VO> list = erp991005Service.erp991005RComdList(erp991003VO);
        return list;
    }

    //공통코드 등록/수정 팝업
    @PostMapping(value = {"erp991005p1"})
    public ModelAndView geErp991005p1(ModelAndView modelAndView, @Valid Erp991005VO erp991005VO) {
        modelAndView.setViewName("erp/erp991005/erp991005p1");
        modelAndView.addObject("sendObject", erp991005VO);
        return modelAndView;
    }


    //등록.수정 팝업에서 수정일경우 내용 불러오기
    @RequestMapping(value = {"erp991005p1ComdSearch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp991005VO erp991005p1ComdSearch(Erp991005VO erp991003VO) {
        erp991003VO = erp991005Service.erp991005p1ComdSearch(erp991003VO);
        return erp991003VO;
    }

    //등록.수정 팝업에서 저장하기
    @RequestMapping(value = {"erp991005p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp991005VO erp991005p1ComdSave(Erp991005VO erp991003VO) {
        erp991003VO = erp991005Service.erp991005p1ComdSave(erp991003VO);
        return erp991003VO;
    }


} 