package com.sonictms.alsys.erp.erp105011.controller;

import com.sonictms.alsys.erp.erp105011.entity.Erp105011VO;
import com.sonictms.alsys.erp.erp105011.service.Erp105011Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp105011Controller {

    private final Erp105011Service erp105011Service;

    /**
     * 택배/직출 출고관리 화면 열기
     */
    @GetMapping(value = {"erp105011"})
    public ModelAndView getErp105011(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105011/erp105011");
        return modelAndView;
    }

    @PostMapping("/erp105011List")
    @ResponseBody
    public List<Erp105011VO> getList(Erp105011VO vo) {
        return erp105011Service.getLocationStockList(vo);
    }

    @PostMapping("/erp105011/transfer")
    @ResponseBody
    public Map<String, Object> transferStock(@RequestBody Erp105011VO vo, HttpSession session) {
        // 세션 사용자 정보 설정
        Object userPk = session.getAttribute("tblUserMId");
        vo.setProcessUser(userPk != null ? userPk.toString() : "SYSTEM");

        return erp105011Service.processStockTransfer(vo);
    }
}