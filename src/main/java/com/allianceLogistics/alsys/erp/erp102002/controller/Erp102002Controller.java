package com.allianceLogistics.alsys.erp.erp102002.controller;

import com.allianceLogistics.alsys.erp.erp102002.entity.Erp102002VO;
import com.allianceLogistics.alsys.erp.erp102002.service.Erp102002Service;
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
public class Erp102002Controller {

    private final Erp102002Service erp102002Service;

    // 알림톡 발송 스케줄 관리 화면 열기
    @GetMapping(value = {"erp102002"})
    public ModelAndView getErp102002(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp102002/erp102002");

        return modelAndView;
    }

    // 알림톡 발송 스케줄 관리 등록/수정 팝업
    @PostMapping(value = {"erp102002p1"})
    public ModelAndView erp102002p1(ModelAndView modelAndView, @Valid Erp102002VO erp102002VO) {
        modelAndView.setViewName("erp/erp102002/erp102002p1");
        modelAndView.addObject("sendObject", erp102002VO);
        return modelAndView;
    }


    // 알림톡 발송 스케줄 수행 날짜 선택 팝업
    @PostMapping(value = {"erp102002p2"})
    public ModelAndView erp102002p2(ModelAndView modelAndView, @Valid Erp102002VO erp102002VO) {
        modelAndView.setViewName("erp/erp102002/erp102002p2");
        modelAndView.addObject("sendObject", erp102002VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp102002List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102002VO> erp102002List(Erp102002VO erp102002VO) {
        List<Erp102002VO> list = erp102002Service.erp102002List(erp102002VO);
        return list;
    }

    // 알림톡 스케줄 실행 대상 조회
    @RequestMapping(value = {"erp102002ScdlExecLoad"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp102002VO> erp102002ScdlExecLoad(Erp102002VO erp102002VO) {
        List<Erp102002VO> list = erp102002Service.erp102002ScdlExecLoad(erp102002VO);
        return list;
    }

    // 팝업 등록/ 수정 하기
    @RequestMapping(value = {"erp102002p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102002VO erp102002p1ComdSave(Erp102002VO erp102002VO) {
        erp102002VO = erp102002Service.erp102002p1ComdSave(erp102002VO);
        return erp102002VO;
    }

    // 고유 ID 로 정보 찾기
    @RequestMapping(value = {"erp102002p1AlrmSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102002VO erp102002p1AlrmSrch(Erp102002VO erp102002VO) {
        erp102002VO = erp102002Service.erp102002p1AlrmSrch(erp102002VO);
        return erp102002VO;
    }

}