package com.sonictms.alsys.erp.erp901002.controller;

import com.sonictms.alsys.erp.erp901002.entity.Erp901002ExcelVO;
import com.sonictms.alsys.erp.erp901002.entity.Erp901002VO;
import com.sonictms.alsys.erp.erp901002.service.Erp901002Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp901002Controller {

    private final Erp901002Service erp901002Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp901002"})
    public ModelAndView getErp901002(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp901002/erp901002");

        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp901002List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901002VO> erp901002List(Erp901002VO erp901002VO) {
        List<Erp901002VO> list = erp901002Service.erp901002List(erp901002VO);
        return list;
    }

    // 등록/수정 팝업 열기
    @PostMapping(value = {"erp901002p1"})
    public ModelAndView erp901002p1(ModelAndView modelAndView, @Valid Erp901002VO erp901002VO) {
        modelAndView.setViewName("erp/erp901002/erp901002p1");
        modelAndView.addObject("sendObject", erp901002VO);
        return modelAndView;
    }


    // 엑셀업로드 팝업열기
    @PostMapping(value = {"erp901002p2"})
    public ModelAndView erp901002p2(ModelAndView modelAndView, @Valid Erp901002VO erp901002VO) {
        modelAndView.setViewName("erp/erp901002/erp901002p2");
        modelAndView.addObject("sendObject", erp901002VO);
        return modelAndView;
    }


    // 팝업 등록/ 수정 하기
    @RequestMapping(value = {"erp901002p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp901002VO erp901002p1ComdSave(Erp901002VO erp901002VO) {
        erp901002VO = erp901002Service.erp901002p1ComdSave(erp901002VO);
        return erp901002VO;
    }

    // 고유 ID 로 정보 찾기
    @RequestMapping(value = {"erp901002p1mtrlSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp901002VO erp901002p1mtrlSrch(Erp901002VO erp901002VO) {
        erp901002VO = erp901002Service.erp901002p1mtrlSrch(erp901002VO);
        return erp901002VO;
    }


    //엡셀업로드 팝업에서 엑셀데이터를 json으로 바꾼것을 저장하기
    @RequestMapping(value = {"erp901002p2ExcelUploadJson"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp901002ExcelVO> erp901002p2ExcelUploadJson(@RequestBody List<Erp901002ExcelVO> erp901002ExcelVO) {


        for (int i = 0; i < erp901002ExcelVO.size(); i++) {

            erp901002ExcelVO.set(i, erp901002Service.erp901002p2ExcelUploadJson(erp901002ExcelVO.get(i)));
        }


        return erp901002ExcelVO;

    }

}