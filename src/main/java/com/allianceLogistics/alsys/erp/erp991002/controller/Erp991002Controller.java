package com.allianceLogistics.alsys.erp.erp991002.controller;

import com.allianceLogistics.alsys.erp.erp991002.entity.Erp991002VO;
import com.allianceLogistics.alsys.erp.erp991002.service.Erp991002Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp991002Controller {

    private final Erp991002Service erp991002Service;

    // 메뉴-권한관리 화면 열기
    @GetMapping(value = {"erp991002"})
    public ModelAndView geErp991002(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp991002/erp991002");
        return modelAndView;
    }

    // 메뉴 권한그룹 추가 팝업
    @PostMapping(value = {"erp991002p1"})
    public ModelAndView geErp991002p1(ModelAndView modelAndView, @Valid Erp991002VO erp991002VO) {
        modelAndView.setViewName("erp/erp991002/erp991002p1");
        modelAndView.addObject("sendObject", erp991002VO);
        return modelAndView;
    }

    // 메뉴권한그룹 그리드 리스트 불러오기
    @RequestMapping(value = {"erp991002List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp991002VO> erp991002List(Erp991002VO erp991002VO) {
        List<Erp991002VO> list = erp991002Service.erp991002List(erp991002VO);
        return list;
    }

    // 메뉴권한그룹 입력하기
    @RequestMapping(value = {"erp991002Updt"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp991002VO erp991002Updt(@RequestBody List<Erp991002VO> list, HttpSession session) {
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSaveUser(session.getAttribute("userId").toString()); // 세션에서 로그인한 아이디 찾아 저장자로 넣기
            if (list.get(i).getChk().equals("1")) {
                list.get(i).setChk("Y");
            } else {
                list.get(i).setChk("N");
            }

        }

        Erp991002VO erp991002VO = erp991002Service.erp991002Updt(list);
        return erp991002VO;
    }

    // 메뉴 권한그룹 추가 팝업에서 간편 권한그룹 중복체크
    @RequestMapping(value = {"erp991002p1DoubleChk"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp991002VO erp991002p1DoubleChk(Erp991002VO erp991002VO) {

        erp991002VO = erp991002Service.erp991002p1DoubleChk(erp991002VO);
        return erp991002VO;
    }

    // 권한그룹 간편 추가 하기
    @RequestMapping(value = {"erp991002p1Save"}, method = RequestMethod.POST)
    @ResponseBody

    public Erp991002VO erp991002p1Save(Erp991002VO erp991002VO) {

        erp991002VO = erp991002Service.erp991002p1Save(erp991002VO);
        return erp991002VO;
    }

}