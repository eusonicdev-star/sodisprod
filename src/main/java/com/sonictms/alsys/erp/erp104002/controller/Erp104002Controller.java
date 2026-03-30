package com.sonictms.alsys.erp.erp104002.controller;

import com.sonictms.alsys.erp.erp104002.entity.Erp104002VO;
import com.sonictms.alsys.erp.erp104002.service.Erp104002Service;
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
public class Erp104002Controller {

    private final Erp104002Service erp104002Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp104002"})
    public ModelAndView getErp104002(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp104002/erp104002");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp104002List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp104002VO> erp104002List(Erp104002VO erp104002VO) {
        List<Erp104002VO> list = erp104002Service.erp104002List(erp104002VO);
        return list;
    }
}