package com.allianceLogistics.alsys.erp.erp991004.controller;

import com.allianceLogistics.alsys.erp.erp991004.entity.Erp991004VO;
import com.allianceLogistics.alsys.erp.erp991004.service.Erp991004Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp991004Controller {

    private final Erp991004Service erp991004Service;

    // 사용자권한관리 화면 열기
    @GetMapping(value = {"erp991004"})
    public ModelAndView geErp991004(ModelAndView modelAndView, @Valid Erp991004VO erp991004VO) {
        modelAndView.setViewName("erp/erp991004/erp991004");
        modelAndView.addObject("sendObject", erp991004VO);
        return modelAndView;
    }

    // 메뉴권한그룹 그리드 리스트 불러오기
    @RequestMapping(value = {"erp991004List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp991004VO> erp991004List(Erp991004VO erp991004VO) {
        List<Erp991004VO> list = erp991004Service.erp991004List(erp991004VO);
        return list;
    }

}