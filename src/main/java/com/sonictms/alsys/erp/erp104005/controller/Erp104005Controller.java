package com.sonictms.alsys.erp.erp104005.controller;

import com.sonictms.alsys.erp.erp104005.entity.Erp104005VO;
import com.sonictms.alsys.erp.erp104005.service.Erp104005Service;
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
public class Erp104005Controller {

    private final Erp104005Service erp104005Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp104005"})
    public ModelAndView getErp104005(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp104005/erp104005");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp104005List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp104005VO> erp104005List(Erp104005VO erp104005VO) {
        List<Erp104005VO> list = erp104005Service.erp104005List(erp104005VO);
        return list;
    }

}