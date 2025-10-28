package com.sonictms.alsys.erp.erp105007.controller;

import com.sonictms.alsys.erp.erp105007.entity.Erp105007VO;
import com.sonictms.alsys.erp.erp105007.service.Erp105007Service;
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
public class Erp105007Controller {

    private final Erp105007Service erp105007Service;

    // 미확정리스트 화면 열기
    @GetMapping(value = {"erp105007"})
    public ModelAndView getErp105007(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105007/erp105007");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp105007List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105007VO> erp105007List(Erp105007VO erp105007VO) {
        List<Erp105007VO> list = erp105007Service.erp105007List(erp105007VO);
        return list;
    }
}
