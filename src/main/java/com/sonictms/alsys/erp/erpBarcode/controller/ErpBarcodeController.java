package com.sonictms.alsys.erp.erpBarcode.controller;

import com.sonictms.alsys.erp.erpBarcode.entity.ErpBarcodeVO;
import com.sonictms.alsys.erp.erpBarcode.service.ErpBarcodeService;
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
public class ErpBarcodeController {

    private final ErpBarcodeService erpBarcodeService;

    //공통코드관리 화면 열기
    @GetMapping(value = {"erpBarcode"})
    public ModelAndView getErpBarcode(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erpBarcode/erpBarcode");

        return modelAndView;
    }


    //왼쪽 마스터 공통목록 열기
    @RequestMapping(value = {"erpBarcodeLCommList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<ErpBarcodeVO> erpBarcodeLCommList(ErpBarcodeVO erp991003VO) {
        List<ErpBarcodeVO> list = erpBarcodeService.erpBarcodeLCommList(erp991003VO);
        return list;
    }

    //오른쪽 상세 공통목록 열기
    @RequestMapping(value = {"erpBarcodeRComdList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<ErpBarcodeVO> erpBarcodeRComdList(ErpBarcodeVO erp991003VO) {
        List<ErpBarcodeVO> list = erpBarcodeService.erpBarcodeRComdList(erp991003VO);
        return list;
    }

    //공통코드 등록/수정 팝업
    @PostMapping(value = {"erpBarcodep1"})
    public ModelAndView geErpBarcodep1(ModelAndView modelAndView, @Valid ErpBarcodeVO erpBarcodeVO) {
        modelAndView.setViewName("erp/erpBarcode/erpBarcodep1");
        modelAndView.addObject("sendObject", erpBarcodeVO);
        return modelAndView;
    }


    //등록.수정 팝업에서 수정일경우 내용 불러오기
    @RequestMapping(value = {"erpBarcodep1ComdSearch"}, method = RequestMethod.POST)
    @ResponseBody
    public ErpBarcodeVO erpBarcodep1ComdSearch(ErpBarcodeVO erp991003VO) {
        erp991003VO = erpBarcodeService.erpBarcodep1ComdSearch(erp991003VO);
        return erp991003VO;
    }

    //등록.수정 팝업에서 저장하기
    @RequestMapping(value = {"erpBarcodep1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody
    public ErpBarcodeVO erpBarcodep1ComdSave(ErpBarcodeVO erp991003VO) {
        erp991003VO = erpBarcodeService.erpBarcodep1ComdSave(erp991003VO);
        return erp991003VO;
    }


} 