package com.sonictms.alsys.erp.erp901004.controller;

import com.sonictms.alsys.erp.erp901004.entity.Erp901004ExcelVO;
import com.sonictms.alsys.erp.erp901004.entity.Erp901004VO;
import com.sonictms.alsys.erp.erp901004.service.Erp901004Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp901004Controller {

    private final Erp901004Service erp901004Service;

    // 메인 화면 열기
    @GetMapping(value = {"erp901004"})
    public ModelAndView getErp901004(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp901004/erp901004");

        return modelAndView;
    }

    // 등록/수정 팝업 화면열기
    @PostMapping(value = {"erp901004p1"})
    public ModelAndView erp901004p1(ModelAndView modelAndView, @Valid Erp901004VO erp901004VO) {
        modelAndView.setViewName("erp/erp901004/erp901004p1");
        modelAndView.addObject("sendObject", erp901004VO);
        return modelAndView;
    }


    // 엑셀업로드 팝업열기
    @PostMapping(value = {"erp901004p2"})
    public ModelAndView erp901004p2(ModelAndView modelAndView, @Valid Erp901004VO erp901004VO) {
        modelAndView.setViewName("erp/erp901004/erp901004p2");
        modelAndView.addObject("sendObject", erp901004VO);
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp901004List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901004VO> erp901004List(Erp901004VO erp901004VO) {
        List<Erp901004VO> list = erp901004Service.erp901004List(erp901004VO);
        return list;
    }

    // 팝업 왼쪽 하위 품목 찾기
    @RequestMapping(value = {"erp901004p1ComdSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901004VO> erp901004p1ComdSrch(Erp901004VO erp901004VO) {
        List<Erp901004VO> list = erp901004Service.erp901004p1ComdSrch(erp901004VO);
        return list;
    }

    // 팝업 등록/ 수정 하기
    @RequestMapping(value = {"erp901004p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901004VO> erp901004p1ComdSave(@RequestBody List<Erp901004VO> erp901004VO) { // 화면에서 데이터를 json 형태로
        // 보내는데 그게 어레이 일떄는
        // @RequestBody 를 붙여 받는다

        for (int i = 0; i < erp901004VO.size(); i++) {
            erp901004VO.set(i, erp901004Service.erp901004p1ComdSave(erp901004VO.get(i)));

        }

        return erp901004VO;
    }

    // 정보 찾기
    @RequestMapping(value = {"erp901004prntMtrlList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp901004VO> erp901004prntMtrlList(Erp901004VO erp901004VO) {
        List<Erp901004VO> list = erp901004Service.erp901004prntMtrlList(erp901004VO);
        return list;
    }

    // 로우 삭제
/*
	@RequestMapping(value = { "erp901004p1DelTblBomId" }, method = RequestMethod.POST)
	@ResponseBody
	public Erp901004VO erp901004p1DelTblBomId(Erp901004VO erp901004VO) {
		erp901004VO = erp901004Service.erp901004p1DelTblBomId(erp901004VO);
		return erp901004VO;
	}
	*/

    //엡셀업로드 팝업에서 엑셀데이터를 json으로 바꾼것을 저장하기
    @RequestMapping(value = {"erp901004p2ExcelUploadJson"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp901004ExcelVO> erp901004p2ExcelUploadJson(@RequestBody List<Erp901004ExcelVO> erp901004ExcelVO) {


        for (int i = 0; i < erp901004ExcelVO.size(); i++) {

            erp901004ExcelVO.set(i, erp901004Service.erp901004p2ExcelUploadJson(erp901004ExcelVO.get(i)));
        }


        return erp901004ExcelVO;

    }


}