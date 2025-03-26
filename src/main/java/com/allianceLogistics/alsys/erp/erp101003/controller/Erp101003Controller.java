package com.allianceLogistics.alsys.erp.erp101003.controller;

import com.allianceLogistics.alsys.erp.erp101003.entity.Erp101003VO;
import com.allianceLogistics.alsys.erp.erp101003.service.Erp101003Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp101003Controller {

    private final Erp101003Service erp101003Service;

    // 공통코드관리 화면 열기
    @GetMapping("erp101003")
    public ModelAndView getErp101003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp101003/erp101003");
        return modelAndView;
    }

    // 리스트조회
    @PostMapping("erp101003List")
    @ResponseBody
    public List<Erp101003VO> erp101003List(Erp101003VO erp101003VO) {
        return erp101003Service.erp101003List(erp101003VO);
    }

    // 체크한것들 삭제(일괄삭제)
    @ResponseBody
    @PostMapping("erp101003ChkDel")
    public List<Erp101003VO> erp101003ChkDel(@RequestBody List<Erp101003VO> erp101003VO) {
        IntStream.range(0, erp101003VO.size()).forEach(i -> {
            String soNo = erp101003VO.get(i).getSoNo();
            erp101003VO.set(i, erp101003Service.erp101003ChkDel(erp101003VO.get(i)));
            erp101003VO.get(i).setSoNo(soNo);
        });
        return erp101003VO;
    }

    // 체크한것들 변경(일괄변경)
    @ResponseBody
    @PostMapping("erp101003chkUpdt")
    public List<Erp101003VO> erp101003chkUpdt(@RequestBody List<Erp101003VO> erp101003VO) {
        for (int i = 0; i < erp101003VO.size(); i++) {
            String soNo = erp101003VO.get(i).getSoNo();
            erp101003VO.set(i, erp101003Service.erp101003chkUpdt(erp101003VO.get(i)));
            erp101003VO.get(i).setSoNo(soNo);
        }

        return erp101003VO;
    }


    //20211226 정연호 추가.  체크한것들 주문복사
    @ResponseBody
    @PostMapping("erp101003chkCopy")
    public List<Erp101003VO> erp101003chkCopy(@RequestBody List<Erp101003VO> erp101003VO) {
        for (int i = 0; i < erp101003VO.size(); i++) {
            String soNo = erp101003VO.get(i).getSoNo();
            erp101003VO.set(i, erp101003Service.erp101003chkCopy(erp101003VO.get(i)));
            erp101003VO.get(i).setSoNo(soNo);
        }

        return erp101003VO;
    }


    // 주문 수정 팝업 화면 열기
    @PostMapping("erp101003p3")
    public ModelAndView erp101003p3(ModelAndView modelAndView, @Valid Erp101003VO erp101003VO) {
        modelAndView.setViewName("erp/erp101001/erp101001p3");
        modelAndView.addObject("sendObject", erp101003VO);
        return modelAndView;
    }
}