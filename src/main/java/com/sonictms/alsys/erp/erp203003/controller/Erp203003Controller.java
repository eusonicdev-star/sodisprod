package com.sonictms.alsys.erp.erp203003.controller;

import com.sonictms.alsys.erp.erp203003.entity.Erp203003VO;
import com.sonictms.alsys.erp.erp203003.service.Erp203003Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp203003Controller {

    private final Erp203003Service erp203003Service;

    // 메인 화면 열기
    @GetMapping(value = {"erp203003"})
    public ModelAndView getErp203003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp203003/erp203003");
        return modelAndView;
    }

    //메인화면 결과값 조회
    @RequestMapping(value = {"erp203003List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203003VO> erp203003List(Erp203003VO erp203003VO) {
        List<Erp203003VO> list = erp203003Service.erp203003List(erp203003VO);
        return list;
    }

    //오른쪽화면 응답률 조회
    @RequestMapping(value = {"erp203003List2"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp203003VO> erp203003List2(Erp203003VO erp203003VO) {
        List<Erp203003VO> list = erp203003Service.erp203003List2(erp203003VO);
        return list;
    }
}