package com.allianceLogistics.alsys.erp.erp902004.controller;

import com.allianceLogistics.alsys.erp.erp902004.entity.Erp902004VO;
import com.allianceLogistics.alsys.erp.erp902004.service.Erp902004Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp902004Controller {

    private final Erp902004Service erp902004Service;

    // 화면오픈
    @GetMapping(value = {"erp902004"})
    public ModelAndView getErp902004(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp902004/erp902004");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp902004List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902004VO> erp902004List(Erp902004VO erp902004VO) {
        List<Erp902004VO> list = erp902004Service.erp902004List(erp902004VO);
        return list;
    }

    // 물류센터매핑저장하기

    @RequestMapping(value = {"erp902004Save"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902004VO> erp902004Save(@RequestBody List<Erp902004VO> erp902004VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다

        for (int i = 0; i < erp902004VO.size(); i++) {
            erp902004VO.set(i, erp902004Service.erp902004Save(erp902004VO.get(i)));

        }
        return erp902004VO;
    }

    // 물류센터매핑삭제하기

    @RequestMapping(value = {"erp902004Delete"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902004VO> erp902004Delete(@RequestBody List<Erp902004VO> erp902004VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다

        for (int i = 0; i < erp902004VO.size(); i++) {
            erp902004VO.set(i, erp902004Service.erp902004Delete(erp902004VO.get(i)));

        }
        return erp902004VO;
    }

}