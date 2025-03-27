package com.sonictms.alsys.erp.erp991003.controller;

import com.sonictms.alsys.erp.erp991003.entity.Erp991003VO;
import com.sonictms.alsys.erp.erp991003.service.Erp991003Service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp991003Controller {

    private final Erp991003Service erp991003Service;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 사용자권한관리 화면 열기
    @GetMapping(value = {"erp991003"})
    public ModelAndView geErp991003(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp991003/erp991003");
        return modelAndView;
    }

    // 사용자권한관리 등록 팝업
    @PostMapping(value = {"erp991003p1"})
    public ModelAndView geErp991003p1(ModelAndView modelAndView, @Valid Erp991003VO erp991003VO) {
        modelAndView.setViewName("erp/erp991003/erp991003p1");
        modelAndView.addObject("sendObject", erp991003VO);
        return modelAndView;
    }

    // 메뉴권한그룹 그리드 리스트 불러오기
    @RequestMapping(value = {"erp991003List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp991003VO> erp991003List(Erp991003VO erp991003VO) {
        List<Erp991003VO> list = erp991003Service.erp991003List(erp991003VO);
        return list;
    }

    // 사용자 등록/수정
    @RequestMapping(value = {"erp991003p1ComdSave"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp991003VO erp991003p1ComdSave(Erp991003VO erp991003VO) {
        if (erp991003VO.getSaveGubn().equals("INS")) {
            erp991003VO.setUserPw(bCryptPasswordEncoder.encode(erp991003VO.getUserPw())); // 암호를 암호화해서 암호에 넣음
        }

        erp991003VO = erp991003Service.erp991003p1ComdSave(erp991003VO);
        return erp991003VO;
    }

    // 회사코드와 사용자 아이디로 아이디 중복을 체크
    @RequestMapping(value = {"erp991003p1DoubleChk"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp991003VO erp991003p1DoubleChk(Erp991003VO erp991003VO) {

        erp991003VO = erp991003Service.erp991003p1DoubleChk(erp991003VO);
        return erp991003VO;
    }

    // 비밀번호 초기화 - 비밀번호를 아이디와 똑같게 만듦
    @RequestMapping(value = {"erp991003p1PwReset"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp991003VO erp991003p1PwReset(Erp991003VO erp991003VO) {

        erp991003VO.setUserPw(bCryptPasswordEncoder.encode(erp991003VO.getUserId())); // ID를 암호화해서 암호에 넣음
        erp991003VO = erp991003Service.erp991003p1PwReset(erp991003VO);
        return erp991003VO;
    }

    // tblUserMId 로 사용자 정보 불러오기
    @RequestMapping(value = {"erp991003p1TblUserMId"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp991003VO erp991003p1TblUserMId(Erp991003VO erp991003VO) {

        erp991003VO = erp991003Service.erp991003p1TblUserMId(erp991003VO);
        return erp991003VO;
    }

}