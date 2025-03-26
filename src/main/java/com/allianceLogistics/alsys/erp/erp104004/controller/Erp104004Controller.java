package com.allianceLogistics.alsys.erp.erp104004.controller;

import com.allianceLogistics.alsys.erp.erp104004.entity.Erp104004VO;
import com.allianceLogistics.alsys.erp.erp104004.service.Erp104004Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
public class Erp104004Controller {

    private final Erp104004Service erp104004Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp104004"})
    public ModelAndView getErp104004(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp104004/erp104004");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp104004List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp104004VO> erp104004List(Erp104004VO erp104004VO) {
        List<Erp104004VO> list = erp104004Service.erp104004List(erp104004VO);
        return list;
    }

}