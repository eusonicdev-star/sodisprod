package com.sonictms.alsys.erp.erp105006.controller;

import com.sonictms.alsys.erp.erp105006.entity.Erp105006VO;
import com.sonictms.alsys.erp.erp105006.service.Erp105006Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Controller
public class Erp105006Controller {

    private final Erp105006Service erp105006Service;
    
    /**
     * 사용자 정보를 "유진(799)" 형태로 변환
     * @param userInfo 원본 사용자 정보 (예: "A_erp_799_유진")
     * @return 변환된 사용자 정보 (예: "유진(799)")
     */
    private String formatUserInfo(String userInfo) {
        if (userInfo == null || userInfo.isEmpty()) {
            return userInfo;
        }
        
        // A_erp_799_유진 형태인 경우
        if (userInfo.startsWith("A_erp_") && userInfo.contains("_")) {
            String[] parts = userInfo.split("_");
            if (parts.length >= 4) {
                String userId = parts[2]; // 799
                String userName = parts[3]; // 유진
                return userName + "(" + userId + ")";
            }
        }
        
        return userInfo;
    }

    /**
     * 재입고 대기 관리 화면 열기
     * @param modelAndView ModelAndView
     * @return 화면 경로
     */
    @GetMapping(value = {"erp105006"})
    public ModelAndView getErp105006(ModelAndView modelAndView) {
        log.info("재입고 대기 관리 화면 열기");
        modelAndView.setViewName("erp/erp105006/erp105006");
        return modelAndView;
    }

    /**
     * 재입고 대기 목록 조회
     * @param erp105006VO 검색 조건
     * @param request HttpServletRequest
     * @return 재입고 대기 목록
     */
    @RequestMapping(value = {"erp105006List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105006VO> erp105006List(Erp105006VO erp105006VO, HttpServletRequest request) {
        log.info("재입고 대기 목록 조회 요청");
        log.info("검색 조건: {}", erp105006VO);
        
        // 세션에서 사용자 권한 정보 가져오기
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        String agntCd = (String) request.getSession().getAttribute("agntCd");
        
        log.info("사용자 권한 정보 - userGrntCd: {}, userGrdCd: {}, agntCd: {}", userGrntCd, userGrdCd, agntCd);
        
        // 화주사 권한인 경우 본인 화주사로만 조회 강제
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd)) {
            log.info("화주사 권한 - 본인 화주사로만 조회 강제: {}", agntCd);
            erp105006VO.setAgntList(agntCd);
        }
        
        // 지방물류센터 권한인 경우 본인 물류센터로만 조회 강제
        if ("2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            String dcCd = (String) request.getSession().getAttribute("dcCd");
            log.info("지방물류센터 권한 - 본인 물류센터로만 조회 강제: {}", dcCd);
            erp105006VO.setDcList(dcCd);
        }
        
        List<Erp105006VO> list = erp105006Service.erp105006List(erp105006VO);
        log.info("조회 결과: {}건", list.size());
        
        return list;
    }

    /**
     * 재입고 완료 처리
     * @param erp105006VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105006InboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105006InboundComplete(Erp105006VO erp105006VO, HttpServletRequest request) {
        log.info("재입고 완료 처리 요청");
        log.info("처리 정보: {}", erp105006VO);
        
        // 권한 체크: 화주사와 지방물류센터는 접근 불가
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd) || 
            "2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            log.warn("권한 없음 - 화주사/지방물류센터는 재입고 완료 처리 불가");
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 용인센터 직원만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105006VO.getProcessUser() != null) {
            erp105006VO.setProcessUser(formatUserInfo(erp105006VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105006Service.erp105006InboundComplete(erp105006VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "재입고 완료 처리되었습니다.");
            result.put("processedCount", processedCount);
            log.info("재입고 완료 처리 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("재입고 완료 처리 실패", e);
        }
        
        return result;
    }

    /**
     * 반출 완료 처리
     * @param erp105006VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105006OutboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105006OutboundComplete(Erp105006VO erp105006VO, HttpServletRequest request) {
        log.info("반출 완료 처리 요청");
        log.info("처리 정보: {}", erp105006VO);
        
        // 권한 체크: 화주사와 지방물류센터는 접근 불가
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd) || 
            "2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            log.warn("권한 없음 - 화주사/지방물류센터는 반출 완료 처리 불가");
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 용인센터 직원만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105006VO.getProcessUser() != null) {
            erp105006VO.setProcessUser(formatUserInfo(erp105006VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105006Service.erp105006OutboundComplete(erp105006VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "반출 완료 처리되었습니다.");
            result.put("processedCount", processedCount);
            log.info("반출 완료 처리 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("반출 완료 처리 실패", e);
        }
        
        return result;
    }

    /**
     * 일괄 재입고 완료 처리
     * @param erp105006VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105006BatchInboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105006BatchInboundComplete(Erp105006VO erp105006VO, HttpServletRequest request) {
        log.info("일괄 재입고 완료 처리 요청");
        log.info("처리 정보: {}", erp105006VO);
        
        // 권한 체크: 화주사와 지방물류센터는 접근 불가
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd) || 
            "2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            log.warn("권한 없음 - 화주사/지방물류센터는 일괄 재입고 완료 처리 불가");
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 용인센터 직원만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105006VO.getProcessUser() != null) {
            erp105006VO.setProcessUser(formatUserInfo(erp105006VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105006Service.erp105006BatchInboundComplete(erp105006VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "재입고 완료 처리되었습니다.");
            result.put("processedCount", processedCount);
            log.info("일괄 재입고 완료 처리 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("일괄 재입고 완료 처리 실패", e);
        }
        
        return result;
    }

    /**
     * 일괄 반출 완료 처리
     * @param erp105006VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105006BatchOutboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105006BatchOutboundComplete(Erp105006VO erp105006VO, HttpServletRequest request) {
        log.info("일괄 반출 완료 처리 요청");
        log.info("처리 정보: {}", erp105006VO);
        
        // 권한 체크: 화주사와 지방물류센터는 접근 불가
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd) || 
            "2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            log.warn("권한 없음 - 화주사/지방물류센터는 일괄 반출 완료 처리 불가");
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 용인센터 직원만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105006VO.getProcessUser() != null) {
            erp105006VO.setProcessUser(formatUserInfo(erp105006VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105006Service.erp105006BatchOutboundComplete(erp105006VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "반출 완료 처리되었습니다.");
            result.put("processedCount", processedCount);
            log.info("일괄 반출 완료 처리 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("일괄 반출 완료 처리 실패", e);
        }
        
        return result;
    }

    /**
     * 반출 대상 여부 변경
     * @param erp105006VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105006ToggleOutboundTarget"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105006ToggleOutboundTarget(Erp105006VO erp105006VO, HttpServletRequest request) {
        log.info("반출 대상 여부 변경 요청");
        log.info("처리 정보: {}", erp105006VO);
        
        // 권한 체크: 화주사와 지방물류센터는 접근 불가
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd) || 
            "2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            log.warn("권한 없음 - 화주사/지방물류센터는 반출 대상 여부 변경 불가");
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 용인센터 직원만 사용할 수 있습니다.");
            return result;
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105006Service.erp105006ToggleOutboundTarget(erp105006VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "반출 대상 여부가 변경되었습니다.");
            result.put("processedCount", processedCount);
            log.info("반출 대상 여부 변경 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("반출 대상 여부 변경 실패", e);
        }
        
        return result;
    }
}
