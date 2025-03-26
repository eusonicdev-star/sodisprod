package com.allianceLogistics.alsys.erp.erp203002.controller;

import com.allianceLogistics.alsys.erp.erp203002.entity.Erp203002VO;
import com.allianceLogistics.alsys.erp.erp203002.service.Erp203002Service;
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
public class Erp203002Controller {

    private final Erp203002Service erp203002Service;

    // 메인 화면 열기
    @GetMapping(value = {"erp203002"})
    public ModelAndView getErp203002(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp203002/erp203002");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp203002List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203002VO> erp203002List(Erp203002VO erp203002VO) {
        return erp203002Service.erp203002List(erp203002VO);
    }

    // 체크한것들 확정
    @RequestMapping(value = {"erp203002rowCnfm"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp203002VO> erp203002rowCnfm(@RequestBody List<Erp203002VO> erp203002VO) {
        for (int i = 0; i < erp203002VO.size(); i++) {
            String soNo = erp203002VO.get(i).getSoNo();
            erp203002VO.set(i, erp203002Service.erp203002rowCnfm(erp203002VO.get(i)));
            erp203002VO.get(i).setSoNo(soNo);
        }

        return erp203002VO;
    }

    // 체크한것들 취소
    @RequestMapping(value = {"erp203002rowCnsl"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp203002VO> erp203002rowCnsl(@RequestBody List<Erp203002VO> erp203002VO) {
        for (int i = 0; i < erp203002VO.size(); i++) {
            String soNo = erp203002VO.get(i).getSoNo();
            erp203002VO.set(i, erp203002Service.erp203002rowCnsl(erp203002VO.get(i)));
            erp203002VO.get(i).setSoNo(soNo);
        }

        return erp203002VO;
    }

    // 모바일확정 일괄 확정/취소 팝업 열기
    @PostMapping(value = {"erp203002p1"})
    public ModelAndView erp203002p1(ModelAndView modelAndView, @Valid Erp203002VO erp203002VO) {
        modelAndView.setViewName("erp/erp203002/erp203002p1");
        modelAndView.addObject("sendObject", erp203002VO);
        return modelAndView;
    }

    // 모바일확정 일괄 확정/취소 팝업에서 일괄 확정/취소 하기
    @RequestMapping(value = {"erp203002allCnfmCnsl"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp203002VO erp203002p3DelDetail(Erp203002VO erp203002VO) {
        erp203002VO = erp203002Service.erp203002allCnfmCnsl(erp203002VO);

        return erp203002VO;

    }

}