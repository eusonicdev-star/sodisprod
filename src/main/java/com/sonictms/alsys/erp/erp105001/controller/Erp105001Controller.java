package com.sonictms.alsys.erp.erp105001.controller;

import com.sonictms.alsys.erp.erp105001.entity.Erp105001VO;
import com.sonictms.alsys.erp.erp105001.service.Erp105001Service;
import lombok.RequiredArgsConstructor;
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
public class Erp105001Controller {

    private final Erp105001Service erp105001Service;

    // 재고 현황 조회 화면 열기
    @GetMapping(value = {"erp105001"})
    public ModelAndView getErp105001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105001/erp105001");
        return modelAndView;
    }

    // 재고 현황 리스트 조회
    @RequestMapping(value = {"erp105001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105001VO> erp105001List(Erp105001VO erp105001VO) {
        List<Erp105001VO> list = erp105001Service.erp105001List(erp105001VO);
        return list;
    }

    // 재고 조정
    @PostMapping("erp105001Adjustment")
    @ResponseBody
    public Map<String, Object> erp105001Adjustment(@RequestBody Erp105001VO erp105001VO) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            boolean success = erp105001Service.adjustInventory(erp105001VO);
            result.put("success", success);
            if (success) {
                result.put("message", "재고 조정이 완료되었습니다.");
            } else {
                result.put("message", "재고 조정에 실패했습니다.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "재고 조정 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return result;
    }
}
