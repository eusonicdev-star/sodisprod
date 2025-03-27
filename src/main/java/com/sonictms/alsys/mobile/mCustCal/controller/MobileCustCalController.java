package com.sonictms.alsys.mobile.mCustCal.controller;

import com.sonictms.alsys.erp.erp102001.entity.Erp102001VO;
import com.sonictms.alsys.erp.erp102001.service.Erp102001Service;
import com.sonictms.alsys.mobile.mCustCal.entity.MobileCustCalVO;
import com.sonictms.alsys.mobile.mCustCal.service.MobileCustCalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Controller
public class MobileCustCalController {

    private final MobileCustCalService mobileCustCalService;
    private final Erp102001Service erp102001Service;

    // 고객이 배송일 선택하는 화면열기
    @SuppressWarnings("finally")
    @GetMapping(value = {"mobile/mCustCalPage"})
    public ModelAndView mCustCalPage(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {    //20220315 정연호. httpServletRequest 추가
        try {
            HttpSession session = httpServletRequest.getSession();    //20220311 정연호. httpServletRequest 추가
            if (session != null && session.getAttribute("userName").equals("JSESSIONID_CREATE") && session.getAttribute("sessionChkKey") == null) {
                session.setAttribute("userName", "고객배송일선택");    //20220311 정연호. httpServletRequest 추가
            }
        } catch (Exception e) {
        } finally {
            modelAndView.setViewName("mobile/mCustCal/mCustCalPage");
            return modelAndView;
        }
    }

    // 고객이 배송 가능/불가능 날짜 불러오기
    @SuppressWarnings("finally")
    @RequestMapping(value = {"mobile/mCustCalDisableDay"}, method = RequestMethod.POST)
    @ResponseBody
    public List<MobileCustCalVO> mCustCalDisableDay(@RequestBody MobileCustCalVO mobileCustCalVO, HttpServletRequest httpServletRequest) {    //20220315 정연호. httpServletRequest 추가
        try {
            HttpSession session = httpServletRequest.getSession();    //20220315 정연호. httpServletRequest 추가
            if (session != null && session.getAttribute("userName").toString().indexOf("고객배송일선택") != -1) {
                session.setAttribute("userName", "고객배송일선택_soNo_" + mobileCustCalVO.getSoNo());    //20220315 정연호. httpServletRequest 추가
            }
        } catch (Exception e) {
        } finally {
            List<MobileCustCalVO> list = mobileCustCalService.mCustCalDisableDay(mobileCustCalVO);
            return list;
        }
    }

    // 배송날짜 저장하기 (실제는 CAPA_ID 저장하기)
    @RequestMapping(value = {"mobile/mCustCalSaveDate"}, method = RequestMethod.POST)
    @ResponseBody

    public MobileCustCalVO mCustCalSaveDate(@RequestBody MobileCustCalVO mobileCustCalVO) {
        mobileCustCalVO = mobileCustCalService.mCustCalSaveDate(mobileCustCalVO);
        return mobileCustCalVO;
    }

    //20220221 정연호 추가. 고객이 선택한 배송일을 피드백 해주는 알림톡 내용을 부르는 부분
    // 알림톡 발송할 내용을 불러온다. 기존의 erp102001p4LoadAlrmTmp 을 활용한다
    @RequestMapping(value = {"mobile/mCustTalk"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO mCustTalk(Erp102001VO erp102001VO) {
        erp102001VO = erp102001Service.erp102001p4LoadAlrmTmp(erp102001VO);
        return erp102001VO;
    }

    //20220224 정연호 고객이 기사평가하는 화면 열기
    @SuppressWarnings("finally")
    @GetMapping(value = {"mobile/mCustFeedBack"})
    public ModelAndView mCustFeedBack(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {//20220315 정연호. httpServletRequest 추가
        try {
            HttpSession session = httpServletRequest.getSession();    //20220315 정연호. httpServletRequest 추가
            if (session != null && session.getAttribute("userName").equals("JSESSIONID_CREATE") && session.getAttribute("sessionChkKey") == null) {
                session.setAttribute("userName", "고객기사평가");    //20220315 정연호. httpServletRequest 추가
            }
        } catch (Exception e) {
        } finally {
            modelAndView.setViewName("mobile/mCustCal/mCustFeedBack");
            return modelAndView;
        }
    }

    //20220224 정연호. 기사평가 신규
    @SuppressWarnings("finally")
    //20220224 정연호 신규. 평가를 했나 안했나 조회하는것
    @RequestMapping(value = {"mobile/mFeedSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileCustCalVO mFeedSrch(@RequestBody MobileCustCalVO mobileCustCalVO, HttpServletRequest httpServletRequest) {    //20220315 정연호. httpServletRequest 추가
        try {
            HttpSession session = httpServletRequest.getSession();    //20220315 정연호. httpServletRequest 추가
            if (session != null && session.getAttribute("userName").toString().indexOf("고객기사평가") != -1) {
                session.setAttribute("userName", "고객기사평가_tblSoMId_" + mobileCustCalVO.getTblSoMId());    //20220315 정연호. httpServletRequest 추가
            }
        } catch (Exception e) {
        } finally {
            mobileCustCalVO = mobileCustCalService.mFeedSrch(mobileCustCalVO);
            return mobileCustCalVO;
        }
    }

    //20220224 정연호. 기사평가 신규
    //20220224 정연호 신규. 평가 결과 저장하기
    @RequestMapping(value = {"mobile/mSaveFeedBack"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileCustCalVO mSaveFeedBack(@RequestBody MobileCustCalVO mobileCustCalVO) {
        mobileCustCalVO = mobileCustCalService.mSaveFeedBack(mobileCustCalVO);
        return mobileCustCalVO;
    }
}