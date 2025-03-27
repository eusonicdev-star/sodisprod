package com.sonictms.alsys.erp.erp101016.controller;

import com.sonictms.alsys.erp.erp101016.entity.Erp101016VO;
import com.sonictms.alsys.erp.erp101016.service.Erp101016Service;
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
public class Erp101016Controller {

    private final Erp101016Service erp101016Service;

    // 공통코드관리 화면 열기
    @GetMapping(value = {"erp101016"})
    public ModelAndView getErp101016(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp101016/erp101016");
        return modelAndView;
    }

    // 리스트조회
    @RequestMapping(value = {"erp101016List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101016VO> erp101016List(@RequestBody Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Service.erp101016List(erp101016VO);
        return list;
    }

    //조치이력 상세보기 팝업 열기
    @PostMapping(value = {"erp101016p3"})
    public ModelAndView erp101016p3(ModelAndView modelAndView, @Valid Erp101016VO erp101016VO) {
        modelAndView.setViewName("erp/erp101016/erp101016p3");
        modelAndView.addObject("sendObject", erp101016VO);
        return modelAndView;
    }

    //조치이력 상세보기 팝업의 그리드 리스트 조회하기
    @RequestMapping(value = {"erp101016p3List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101016VO> erp101016p3List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Service.erp101016p3List(erp101016VO);
        return list;
    }

    //취할 조치 없음 팝업 화면 열기
    @PostMapping(value = {"erp101016p6"})
    public ModelAndView erp101016p6(ModelAndView modelAndView, @Valid Erp101016VO erp101016VO) {
        modelAndView.setViewName("erp/erp101016/erp101016p6");
        modelAndView.addObject("sendObject", erp101016VO);
        return modelAndView;
    }

    // 취할 조치 없음 팝업의 예 또는 아니오 버튼을 눌러 값을 저장할때 수행함
    @RequestMapping(value = {"erp101016p6Save"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp101016VO erp101016p6Save(@RequestBody Erp101016VO erp101016VO) {
        erp101016VO = erp101016Service.erp101016p6Save(erp101016VO);

        //20220517 정연호 추가 강제롤백
        //log.info("테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
        //TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return erp101016VO;
    }

    //원 오더 찾기 (미마감) 팝업 화면 열기
    @PostMapping(value = {"erp101016p0"})
    public ModelAndView erp101016p0(ModelAndView modelAndView, @Valid Erp101016VO erp101016VO) {
        modelAndView.setViewName("erp/erp101016/erp101016p0");
        modelAndView.addObject("sendObject", erp101016VO);
        return modelAndView;
    }

    //AS 요청 팝업 화면 열기
    @PostMapping(value = {"erp101016p1"})
    public ModelAndView erp101016p1(ModelAndView modelAndView, @Valid Erp101016VO erp101016VO) {
        modelAndView.setViewName("erp/erp101016/erp101016p1");
        modelAndView.addObject("sendObject", erp101016VO);
        return modelAndView;
    }

    // 반품등록 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
    @RequestMapping(value = {"erp101016p1HeaderList"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp101016VO erp101016p1HeaderList(Erp101016VO erp101016VO) {
        erp101016VO = erp101016Service.erp101016p1HeaderList(erp101016VO);
        return erp101016VO;
    }

    // 반품등록 팝업에서 상세 불러오기 //상세는 리스트
    @RequestMapping(value = {"erp101016p1DetailList"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101016VO> erp101016p1DetailList(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Service.erp101016p1DetailList(erp101016VO);
        return list;
    }

    // 우편번호로 물류센터 찾기
    @RequestMapping(value = {"erp101016p1PostCdDcCd"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp101016VO erp101016p1PostCdDcCd(Erp101016VO erp101016VO) {
        erp101016VO = erp101016Service.erp101016p1PostCdDcCd(erp101016VO);
        return erp101016VO;
    }

    // 배송확정일 가능한 날짜 조회
    @RequestMapping(value = {"erp101016p1List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101016VO> erp101016p1List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Service.erp101016p1List(erp101016VO);
        return list;
    }

    // 원오더찾기 팝업에서 리스트조회
    @RequestMapping(value = {"erp101016p0List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp101016VO> erp101016p0List(Erp101016VO erp101016VO) {
        List<Erp101016VO> list = erp101016Service.erp101016p0List(erp101016VO);
        return list;
    }

    // 미마감 주문 저장하기
    @RequestMapping(value = {"erp101016p1Save"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp101016VO erp101016p1Save(@RequestBody Erp101016VO erp101016VO) {
        erp101016VO = erp101016Service.erp101016p1Save(erp101016VO);

        //20220604 정연호 추가 강제롤백
        //////////log.info("미마감 주문 - 테스트용 저장안하려고 강제롤백발생 : TransactionAspectSupport.currentTransactionStatus().setRollbackOnly()");
        //////////TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();

        return erp101016VO;
    }

    //오출고 처리 버튼의 3단계 2000 반품오더 가 끝나면 4단계 수행을 위한 3.5단계 미마감 주문유형 선택 팝업을 연다
    @PostMapping(value = {"erp101016p7"})
    public ModelAndView erp101016p7(ModelAndView modelAndView, @Valid Erp101016VO erp101016VO) {
        modelAndView.setViewName("erp/erp101016/erp101016p7");
        modelAndView.addObject("sendObject", erp101016VO);
        return modelAndView;
    }
}