package com.allianceLogistics.alsys.erp.erp902006.controller;

import com.allianceLogistics.alsys.erp.erp902006.entity.Erp902006VO;
import com.allianceLogistics.alsys.erp.erp902006.service.Erp902006Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp902006Controller {

    private final Erp902006Service erp902006Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp902006"})
    public ModelAndView getErp902006(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp902006/erp902006");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp902006List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902006VO> erp902006List(Erp902006VO erp902006VO) {
        List<Erp902006VO> list = erp902006Service.erp902006List(erp902006VO);
        return list;
    }

    // 일괄저장 하기 버튼
    @RequestMapping(value = {"erp902006Save"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp902006VO> erp902006Save(@RequestBody List<Erp902006VO> erp902006VO) { // 화면에서 데이터를 json 형태로 보내는데 그게
        // 어레이 일떄는 @RequestBody 를 붙여
        // 받는다
        for (int i = 0; i < erp902006VO.size(); i++) {
            erp902006VO.set(i, erp902006Service.erp902006Save(erp902006VO.get(i)));
        }
        return erp902006VO;
    }
}