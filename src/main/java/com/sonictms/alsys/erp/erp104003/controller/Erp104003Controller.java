package com.sonictms.alsys.erp.erp104003.controller;

import com.sonictms.alsys.erp.erp104003.entity.Erp104003VO;
import com.sonictms.alsys.erp.erp104003.service.Erp104003Service;
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
public class Erp104003Controller {

    private final Erp104003Service erp104003Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp104003"})
    public ModelAndView getErp104003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp104003/erp104003");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp104003List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp104003VO> erp104003List(Erp104003VO erp104003VO) {
        List<Erp104003VO> list = erp104003Service.erp104003List(erp104003VO);
        return list;
    }

}