package com.sonictms.alsys.erp.erp101001.controller;

import com.sonictms.alsys.erp.erp101001.entity.Erp101001VO;
import com.sonictms.alsys.erp.erp101001.service.Erp101001Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp101001Controller {

    private final Erp101001Service erp101001Service;

    // 공통코드관리 화면 열기
    @GetMapping("erp101001")
    public String getErp101001() {
        return "erp/erp101001/erp101001";
    }

    // 주문유형 선택 팝업 화면 열기
    @PostMapping("erp101001p1")
    public String erp101001p1(@Valid @ModelAttribute final Erp101001VO erp101001VO, final Model model) {
        model.addAttribute("sendObject", erp101001VO);
        return "erp/erp101001/erp101001p1";
    }

    // 주문 등록 팝업 화면 열기
    @PostMapping("erp101001p2")
    public String erp101001p2(@Valid @ModelAttribute final Erp101001VO erp101001VO, final Model model) {
        model.addAttribute("sendObject", erp101001VO);
        return "erp/erp101001/erp101001p2";
    }

    // 주문 수정 팝업 화면 열기
    @PostMapping("erp101001p3")
    public String erp101001p3(@Valid @ModelAttribute final Erp101001VO erp101001VO, final Model model) {
        model.addAttribute("sendObject", erp101001VO);
        return "erp/erp101001/erp101001p3";
    }

    // 원오더찾기 팝업 화면 열기
    @PostMapping("erp101001p4")
    public String erp101001p4(@Valid @ModelAttribute final Erp101001VO erp101001VO, final Model model) {
        model.addAttribute("sendObject", erp101001VO);
        return "erp/erp101001/erp101001p4";
    }

    /// / 2000 반품오더, 3100 교환오더(일반), 3200 교환오더(반품), 6000 A/S오더 수정등록 팝업 화면 열기
    @PostMapping("erp101001p5")
    public String erp101001p5(@Valid @ModelAttribute final Erp101001VO erp101001VO, final Model model) {
        model.addAttribute("sendObject", erp101001VO);
        return "erp/erp101001/erp101001p5";
    }

    // 리스트조회
    @PostMapping("erp101001List")
    @ResponseBody
    public List<Erp101001VO> erp101001List(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001List(erp101001VO);
    }

    // 배송확정일 가능한 날짜 조회
    @PostMapping("erp101001p1List")
    @ResponseBody
    public List<Erp101001VO> erp101001p1List(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p1List(erp101001VO);
    }

    // 주문 저장하기
    @PostMapping("erp101001p1Save")
    @ResponseBody
    public Erp101001VO erp101001p1Save(@RequestBody Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p1Save(erp101001VO);
    }

    // 반품주문 저장하기
    @PostMapping("erp101001p5Save")
    @ResponseBody
    public Erp101001VO erp101001p5Save(@RequestBody Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p5Save(erp101001VO);
    }

    // 주문수정 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
    @PostMapping("erp101001p3HeaderList")
    @ResponseBody
    public Erp101001VO erp101001p3HeaderList(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p3HeaderList(erp101001VO);
    }

    // 주문수정 팝업에서 상세 불러오기 //상세는 리스트
    @PostMapping("erp101001p3DetailList")
    @ResponseBody
    public List<Erp101001VO> erp101001p3DetailList(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p3DetailList(erp101001VO);
    }

    // 상세(제품정보) 삭제하기
    @PostMapping("erp101001p3DelDetail")
    @ResponseBody
    public Erp101001VO erp101001p3DelDetail(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p3DelDetail(erp101001VO);
    }

    // 주문 수정하기
    @PostMapping("erp101001p3Updt")
    @ResponseBody
    public Erp101001VO erp101001p3Updt(@RequestBody Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p3Updt(erp101001VO);
    }

    // 원오더찾기 팝업에서 리스트조회
    @PostMapping("erp101001p4List")
    @ResponseBody
    public List<Erp101001VO> erp101001p4List(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p4List(erp101001VO);
    }

    // 반품등록 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
    @PostMapping("erp101001p5HeaderList")
    @ResponseBody
    public Erp101001VO erp101001p5HeaderList(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p5HeaderList(erp101001VO);
    }

    // 반품등록 팝업에서 상세 불러오기 //상세는 리스트
    @PostMapping("erp101001p5DetailList")
    @ResponseBody
    public List<Erp101001VO> erp101001p5DetailList(Erp101001VO erp101001VO) {
        return erp101001Service.erp101001p5DetailList(erp101001VO);
    }

    // 우편번호로 물류센터 찾기
    @PostMapping("erp101001p1PostCdDcCd")
    @ResponseBody
    public Erp101001VO erp101001p1PostCdDcCd(Erp101001VO erp101001VO) {
        erp101001VO = erp101001Service.erp101001p1PostCdDcCd(erp101001VO);
        return erp101001VO;
    }

    // 체크한것들 삭제(일괄삭제)
    @PostMapping("erp101001ChkDel")
    @ResponseBody
    public List<Erp101001VO> erp101001ChkDel(@RequestBody List<Erp101001VO> erp101001VO) {
        IntStream.range(0, erp101001VO.size()).forEach(i -> {
            String soNo = erp101001VO.get(i).getSoNo();
            erp101001VO.set(i, erp101001Service.erp101001ChkDel(erp101001VO.get(i)));
            erp101001VO.get(i).setSoNo(soNo);
        });
        return erp101001VO;
    }
}