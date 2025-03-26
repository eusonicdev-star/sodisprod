package com.allianceLogistics.alsys.erp.erp101004.controller;

import com.allianceLogistics.alsys.erp.erp101004.entity.Erp101004VO;
import com.allianceLogistics.alsys.erp.erp101004.service.Erp101004Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp101004Controller {

    private final Erp101004Service erp101004Service;

    // 화면 열기
    @GetMapping(value = {"erp101004"})
    public ModelAndView getErp101004(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp101004/erp101004");
        return modelAndView;
    }

    //그리드 헤더 조회하기
    @RequestMapping(value = {"gridHeaderSrch101004"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101004VO> gridHeaderSrch101004(Erp101004VO erp101004VO) {
        return erp101004Service.gridHeaderSrch101004(erp101004VO);
    }

    //엑셀데이터를 json으로 바꾼것을 체크하기
    @RequestMapping(value = {"erp101004ExcelUploadCheck"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp101004VO> erp101004ExcelUploadCheck(@RequestBody List<Erp101004VO> erp101004VO) {
        for (int i = 0; i < erp101004VO.size(); i++) {
            erp101004VO.set(i, erp101004Service.erp101004ExcelUploadCheck(erp101004VO.get(i)));
        }
        return erp101004VO;
    }

    //엑셀데이터를 json으로 바꾼것을 저장하기
    @RequestMapping(value = {"erp101004ExcelUploadJson"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101004VO> erp101004ExcelUploadJson(@RequestBody List<Erp101004VO> erp101004VO) {
        int errCnt = 0;
        for (int i = 0; i < erp101004VO.size(); i++) {
            erp101004VO.set(i, erp101004Service.erp101004ExcelUploadJson(erp101004VO.get(i)));
            if (!erp101004VO.get(i).getRtnYn().equals("Y")) {        //리턴결과가 Y가 아니면 (실패면)
                errCnt++;
            }
        }

        if (errCnt > 0) {    //에러가 1개라도 발생했다면
            // 강제롤백
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return erp101004VO;
    }
} 