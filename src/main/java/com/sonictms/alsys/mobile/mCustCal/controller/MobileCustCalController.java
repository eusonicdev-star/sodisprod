package com.sonictms.alsys.mobile.mCustCal.controller;

import com.sonictms.alsys.erp.erp102001.entity.Erp102001VO;
import com.sonictms.alsys.erp.erp102001.service.Erp102001Service;
import com.sonictms.alsys.mobile.mCustCal.entity.MobileCustCalVO;
import com.sonictms.alsys.mobile.mCustCal.service.MobileCustCalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class MobileCustCalController {

    private final MobileCustCalService mobileCustCalService;
    private final Erp102001Service erp102001Service;

    /**
     * 고객이 배송일 선택하는 화면 열기
     */
    @GetMapping(value = {"mobile/mCustCalPage"})
    public ModelAndView mCustCalPage(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession();
            if (session != null && "JSESSIONID_CREATE".equals(session.getAttribute("userName")) 
                && session.getAttribute("sessionChkKey") == null) {
                session.setAttribute("userName", "고객배송일선택");
                log.debug("세션 사용자명을 '고객배송일선택'으로 변경");
            }
        } catch (Exception e) {
            log.warn("세션 처리 중 예외 발생", e);
        }
        
        modelAndView.setViewName("mobile/mCustCal/mCustCalPage");
        return modelAndView;
    }

    /**
     * 고객이 배송 가능/불가능 날짜 불러오기
     */
    @RequestMapping(value = {"mobile/mCustCalDisableDay"}, method = RequestMethod.POST)
    @ResponseBody
    public List<MobileCustCalVO> mCustCalDisableDay(@RequestBody MobileCustCalVO mobileCustCalVO, 
                                                   HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession();
            if (session != null && session.getAttribute("userName") != null) {
                String userName = session.getAttribute("userName").toString();
                if (userName.contains("고객배송일선택")) {
                    String newUserName = "고객배송일선택_soNo_" + mobileCustCalVO.getSoNo();
                    session.setAttribute("userName", newUserName);
                    log.debug("세션 사용자명을 '{}'으로 변경", newUserName);
                }
            }
        } catch (Exception e) {
            log.warn("세션 처리 중 예외 발생 - soNo: {}", mobileCustCalVO.getSoNo(), e);
        }
        
        try {
            List<MobileCustCalVO> list = mobileCustCalService.mCustCalDisableDay(mobileCustCalVO);
            log.debug("배송 불가능 날짜 조회 완료 - soNo: {}, 결과 수: {}", mobileCustCalVO.getSoNo(), list.size());
            return list;
        } catch (Exception e) {
            log.error("배송 불가능 날짜 조회 중 예외 발생 - soNo: {}", mobileCustCalVO.getSoNo(), e);
            throw new RuntimeException("배송 불가능 날짜 조회 실패", e);
        }
    }

    /**
     * 배송날짜 저장하기 (실제는 CAPA_ID 저장하기)
     */
    @RequestMapping(value = {"mobile/mCustCalSaveDate"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileCustCalVO mCustCalSaveDate(@RequestBody MobileCustCalVO mobileCustCalVO) {
        try {
            mobileCustCalVO = mobileCustCalService.mCustCalSaveDate(mobileCustCalVO);
            log.info("배송날짜 저장 완료 - soNo: {}, capaId: {}", 
                    mobileCustCalVO.getSoNo(), mobileCustCalVO.getCapaId());
            return mobileCustCalVO;
        } catch (Exception e) {
            log.error("배송날짜 저장 중 예외 발생 - soNo: {}", mobileCustCalVO.getSoNo(), e);
            throw new RuntimeException("배송날짜 저장 실패", e);
        }
    }

    /**
     * 알림톡 발송할 내용을 불러온다.
     * 기존 erp102001p4LoadAlrmTmp 을 활용한다
     */
    @RequestMapping(value = {"mobile/mCustTalk"}, method = RequestMethod.POST)
    @ResponseBody
    public Erp102001VO mCustTalk(Erp102001VO erp102001VO) {
        try {
            erp102001VO = erp102001Service.erp102001p4LoadAlrmTmp(erp102001VO);
            log.debug("알림톡 템플릿 로드 완료");
            return erp102001VO;
        } catch (Exception e) {
            log.error("알림톡 템플릿 로드 중 예외 발생", e);
            throw new RuntimeException("알림톡 템플릿 로드 실패", e);
        }
    }

    /**
     * 고객이 기사평가하는 화면 열기
     */
    @GetMapping(value = {"mobile/mCustFeedBack"})
    public ModelAndView mCustFeedBack(ModelAndView modelAndView, HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession();
            if (session != null && "JSESSIONID_CREATE".equals(session.getAttribute("userName")) 
                && session.getAttribute("sessionChkKey") == null) {
                session.setAttribute("userName", "고객기사평가");
                log.debug("세션 사용자명을 '고객기사평가'로 변경");
            }
        } catch (Exception e) {
            log.warn("세션 처리 중 예외 발생", e);
        }
        
        modelAndView.setViewName("mobile/mCustCal/mCustFeedBack");
        return modelAndView;
    }

    /**
     * 평가를 했나 안했나 조회하는것
     */
    @RequestMapping(value = {"mobile/mFeedSrch"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileCustCalVO mFeedSrch(@RequestBody MobileCustCalVO mobileCustCalVO, 
                                    HttpServletRequest httpServletRequest) {
        try {
            HttpSession session = httpServletRequest.getSession();
            if (session != null && session.getAttribute("userName") != null) {
                String userName = session.getAttribute("userName").toString();
                if (userName.contains("고객기사평가")) {
                    String newUserName = "고객기사평가_tblSoMId_" + mobileCustCalVO.getTblSoMId();
                    session.setAttribute("userName", newUserName);
                    log.debug("세션 사용자명을 '{}'으로 변경", newUserName);
                }
            }
        } catch (Exception e) {
            log.warn("세션 처리 중 예외 발생 - tblSoMId: {}", mobileCustCalVO.getTblSoMId(), e);
        }
        
        try {
            mobileCustCalVO = mobileCustCalService.mFeedSrch(mobileCustCalVO);
            log.debug("기사평가 조회 완료 - tblSoMId: {}", mobileCustCalVO.getTblSoMId());
            return mobileCustCalVO;
        } catch (Exception e) {
            log.error("기사평가 조회 중 예외 발생 - tblSoMId: {}", mobileCustCalVO.getTblSoMId(), e);
            throw new RuntimeException("기사평가 조회 실패", e);
        }
    }

    /**
     * 평가 결과 저장하기
     */
    @RequestMapping(value = {"mobile/mSaveFeedBack"}, method = RequestMethod.POST)
    @ResponseBody
    public MobileCustCalVO mSaveFeedBack(@RequestBody MobileCustCalVO mobileCustCalVO) {
        try {
            mobileCustCalVO = mobileCustCalService.mSaveFeedBack(mobileCustCalVO);
            log.info("기사평가 저장 완료 - tblSoMId: {}", mobileCustCalVO.getTblSoMId());
            return mobileCustCalVO;
        } catch (Exception e) {
            log.error("기사평가 저장 중 예외 발생 - tblSoMId: {}", mobileCustCalVO.getTblSoMId(), e);
            throw new RuntimeException("기사평가 저장 실패", e);
        }
    }
}