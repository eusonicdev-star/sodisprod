package com.sonictms.alsys.erp.erp105013.controller;

import com.sonictms.alsys.erp.erp105013.entity.Erp105013VO;
import com.sonictms.alsys.erp.erp105013.service.Erp105013Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp105013Controller {

    private final Erp105013Service erp105013Service;

    // 화면 열기
    @GetMapping(value = {"erp105013"})
    public ModelAndView getErp105013(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105013/erp105013");

        return modelAndView;
    }

    @RequestMapping(value = {"erp105013TempSave"}, method = RequestMethod.POST)
    @ResponseBody
    public String erp105013TempSave(@RequestBody List<Erp105013VO> list) {
        try {
            erp105013Service.saveTempStock(list);
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @RequestMapping(value = {"erp105013TempList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105013VO> erp105013TempList(Erp105013VO vo) {
        return erp105013Service.getTempStockList(vo);
    }

    @RequestMapping(value = {"erp105013CreateMissingLoc"}, method = RequestMethod.POST)
    @ResponseBody
    public int erp105013CreateMissingLoc(Erp105013VO vo) {
        return erp105013Service.createMissingLocations(vo);
    }

    @RequestMapping(value = {"erp105013CreateFloorLoc"}, method = RequestMethod.POST)
    @ResponseBody
    public String erp105013CreateFloorLoc(Erp105013VO vo) {
        try {
            erp105013Service.createFloorLocations(vo);
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @RequestMapping(value = {"erp105013Confirm"}, method = RequestMethod.POST)
    @ResponseBody
    public String erp105013Confirm(Erp105013VO vo) {
        erp105013Service.confirmFinalStock(vo);
        return "SUCCESS";
    }

}