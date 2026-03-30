package com.sonictms.alsys.erp.erp105009.controller;

import com.sonictms.alsys.erp.erp105009.entity.Erp105009VO;
import com.sonictms.alsys.erp.erp105009.service.Erp105009Service;
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
public class Erp105009Controller {

    private final Erp105009Service erp105009Service;

    // 출고리스트 화면 열기
    @GetMapping(value = {"erp105009"})
    public ModelAndView getErp105009(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105009/erp105009");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp105009List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105009VO> erp105009List(Erp105009VO erp105009VO) {
        List<Erp105009VO> list = erp105009Service.erp105009List(erp105009VO);
        return list;
    }
}

