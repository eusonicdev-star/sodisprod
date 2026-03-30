package com.sonictms.alsys.erp.erp101002.controller;

import com.sonictms.alsys.erp.erp101002.entity.Erp101002VO;
import com.sonictms.alsys.erp.erp101002.service.Erp101002Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp101002Controller {

    private final Erp101002Service erp101002Service;

    // 화면 열기
    @GetMapping("erp101002")
    public String getErp101002() {
        return "erp/erp101002/erp101002";
    }

    //그리드 헤더 조회하기
    @PostMapping("gridHeaderSrch")
    @ResponseBody
    public List<Erp101002VO> gridHeaderSrch(Erp101002VO erp101002VO) {
        return erp101002Service.gridHeaderSrch(erp101002VO);
    }

    //엑셀데이터를 json으로 바꾼것을 체크하기
    @RequestMapping(value = {"erp101002ExcelUploadCheck"}, method = RequestMethod.POST)
    @ResponseBody

    public List<Erp101002VO> erp101002ExcelUploadCheck(@RequestBody List<Erp101002VO> erp101002VO) {
        erp101002VO.replaceAll(erp101002Service::erp101002ExcelUploadCheck);
        return erp101002VO;
    }

    //엑셀데이터를 json으로 바꾼것을 저장하기
    @RequestMapping(value = {"erp101002ExcelUploadJson"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101002VO> erp101002ExcelUploadJson(@RequestBody List<Erp101002VO> erp101002VO) {
        int errCnt = 0;
        for (int i = 0; i < erp101002VO.size(); i++) {
            erp101002VO.set(i, erp101002Service.erp101002ExcelUploadJson(erp101002VO.get(i)));
            if (!erp101002VO.get(i).getRtnYn().equals("Y")) {        //리턴결과가 Y가 아니면 (실패면)
                errCnt++;
            }
        }

        if (errCnt > 0) {    //에러가 1개라도 발생했다면
            log.info("강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }

        return erp101002VO;
    }
}