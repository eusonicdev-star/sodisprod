package com.sonictms.alsys.erp.erp105012.controller;

import com.sonictms.alsys.erp.erp105012.entity.Erp105012VO;
import com.sonictms.alsys.erp.erp105012.service.Erp105012Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp105012Controller {

    @Autowired
    private Erp105012Service erp105012Service;

    // 화면 열기
    @GetMapping(value = {"erp105012"})
    public ModelAndView getErp105012(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105012/erp105012");

        return modelAndView;
    }

    @RequestMapping(value = "/erp105012List", method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105012VO> erp105012List(Erp105012VO vo) {
        return erp105012Service.getProductPickingRackList(vo);
    }

    @RequestMapping(value = "/erp105012StockLocList", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String,Object>> erp105012StockLocList(Erp105012VO vo) {
        // 특정 상품의 현재 재고가 위치한 로케이션 목록 조회
        return erp105012Service.getStockLocationList(vo);
    }


    @RequestMapping(value = "/erp105012FullPickingLocList", method = RequestMethod.POST)
    @ResponseBody
    public List<Map<String, Object>> erp105012FullPickingLocList(Erp105012VO vo) {
        // 재고 유무와 상관없이 PICKING_YN = 'Y'인 전체 로케이션 조회
        return erp105012Service.getFullPickingLocationList(vo);
    }

    @RequestMapping(value = "/erp105012AutoAssign", method = RequestMethod.POST)
    @ResponseBody
    public String erp105012AutoAssign(Erp105012VO vo) {
        try {
            erp105012Service.autoAssignPickingRack(vo);
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR";
        }
    }

    @RequestMapping(value = "/erp105012SaveByCode", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105012SaveByCode(Erp105012VO vo) {
        Map<String, Object> result = new java.util.HashMap<>();
        try {
            // 서비스의 통합된 메서드 호출
            erp105012Service.updateProductPickingRack(vo);
            result.put("status", "SUCCESS");
        } catch (Exception e) {
            result.put("status", "ERROR");
            result.put("message", e.getMessage()); // 서비스에서 던진 "존재하지 않는 로케이션..." 메시지 전달
        }
        return result;
    }

    @RequestMapping(value = "/erp105012UpdateFixedLoc", method = RequestMethod.POST)
    @ResponseBody
    public String erp105012UpdateFixedLoc(Erp105012VO vo) {
        try {
            erp105012Service.updateFixedLocFromFloor(vo);
            return "SUCCESS";
        } catch (Exception e) {
            return "ERROR";
        }
    }
}