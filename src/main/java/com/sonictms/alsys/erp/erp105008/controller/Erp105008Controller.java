package com.sonictms.alsys.erp.erp105008.controller;

import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import com.sonictms.alsys.erp.erp105008.service.Erp105008Service;
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
public class Erp105008Controller {

    private final Erp105008Service erp105008Service;

    // 진행중리스트 화면 열기
    @GetMapping(value = {"erp105008"})
    public ModelAndView getErp105008(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105008/erp105008");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp105008List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105008VO> erp105008List(Erp105008VO erp105008VO) {
        List<Erp105008VO> list = erp105008Service.erp105008List(erp105008VO);
        return list;
    }
}
