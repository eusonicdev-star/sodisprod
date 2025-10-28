package com.sonictms.alsys.erp.erp105005.controller;

import com.sonictms.alsys.erp.erp105005.entity.Erp105005VO;
import com.sonictms.alsys.erp.erp105005.service.Erp105005Service;
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
public class Erp105005Controller {

    private final Erp105005Service erp105005Service;
    
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
     * 교환/반품 재입고 관리 화면 열기
     * @param modelAndView ModelAndView
     * @return 화면 경로
     */
    @GetMapping(value = {"erp105005"})
    public ModelAndView getErp105005(ModelAndView modelAndView) {
        log.info("교환/반품 재입고 관리 화면 열기");
        modelAndView.setViewName("erp/erp105005/erp105005");
        return modelAndView;
    }

    /**
     * 교환/반품 재입고 목록 조회
     * @param erp105005VO 검색 조건
     * @param request HttpServletRequest
     * @return 재입고 목록
     */
    @RequestMapping(value = {"erp105005List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105005VO> erp105005List(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("교환/반품 재입고 목록 조회 요청");
        log.info("검색 조건: {}", erp105005VO);
        
        // 세션에서 사용자 권한 정보 가져오기
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        String agntCd = (String) request.getSession().getAttribute("agntCd");
        
        log.info("사용자 권한 정보 - userGrntCd: {}, userGrdCd: {}, agntCd: {}", userGrntCd, userGrdCd, agntCd);
        
        // 화주사 권한인 경우 본인 화주사로만 조회 강제
        if ("4100".equals(userGrntCd) || "4200".equals(userGrntCd)) {
            log.info("화주사 권한 - 본인 화주사로만 조회 강제: {}", agntCd);
            erp105005VO.setAgntList(agntCd);
        }
        
        // 지방물류센터 권한인 경우 본인 물류센터로만 조회 강제
        if ("2000".equals(userGrdCd) || "6000".equals(userGrntCd)) {
            String dcCd = (String) request.getSession().getAttribute("dcCd");
            log.info("지방물류센터 권한 - 본인 물류센터로만 조회 강제: {}", dcCd);
            erp105005VO.setDcList(dcCd);
        }
        
        List<Erp105005VO> list = erp105005Service.erp105005List(erp105005VO);
        log.info("조회 결과: {}건", list.size());
        
        return list;
    }

    /**
     * 재입고 완료 처리
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005InboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005InboundComplete(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("재입고 완료 처리 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 재입고 완료 처리 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105005VO.getProcessUser() != null) {
            erp105005VO.setProcessUser(formatUserInfo(erp105005VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005InboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005OutboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005OutboundComplete(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("반출 완료 처리 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 반출 완료 처리 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105005VO.getProcessUser() != null) {
            erp105005VO.setProcessUser(formatUserInfo(erp105005VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005OutboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005BatchInboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005BatchInboundComplete(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("일괄 재입고 완료 처리 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 일괄 재입고 완료 처리 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105005VO.getProcessUser() != null) {
            erp105005VO.setProcessUser(formatUserInfo(erp105005VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005BatchInboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005BatchOutboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005BatchOutboundComplete(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("일괄 반출 완료 처리 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 일괄 반출 완료 처리 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105005VO.getProcessUser() != null) {
            erp105005VO.setProcessUser(formatUserInfo(erp105005VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005BatchOutboundComplete(erp105005VO);
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
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005ToggleOutboundTarget"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005ToggleOutboundTarget(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("반출 대상 여부 변경 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 반출 대상 여부 변경 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005ToggleOutboundTarget(erp105005VO);
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

    /**
     * 입고완료 되돌리기 처리
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005RevertInboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005RevertInboundComplete(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("입고완료 되돌리기 처리 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 입고완료 되돌리기 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105005VO.getProcessUser() != null) {
            erp105005VO.setProcessUser(formatUserInfo(erp105005VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005RevertInboundComplete(erp105005VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "입고완료가 되돌려졌습니다.");
            result.put("processedCount", processedCount);
            log.info("입고완료 되돌리기 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("입고완료 되돌리기 실패", e);
        }
        
        return result;
    }

    /**
     * 반출완료 되돌리기 처리
     * @param erp105005VO 처리 정보
     * @param request HttpServletRequest
     * @return 처리 결과
     */
    @RequestMapping(value = {"erp105005RevertOutboundComplete"}, method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> erp105005RevertOutboundComplete(Erp105005VO erp105005VO, HttpServletRequest request) {
        log.info("반출완료 되돌리기 처리 요청");
        log.info("처리 정보: {}", erp105005VO);
        
        // 권한 체크: 권한 9999인 사용자만 접근 가능
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String userGrdCd = (String) request.getSession().getAttribute("userGrdCd");
        
        if (!"9999".equals(userGrntCd)) {
            log.warn("권한 없음 - 권한 9999인 사용자만 반출완료 되돌리기 가능. 현재 권한: {}", userGrntCd);
            Map<String, Object> result = new HashMap<>();
            result.put("rtnYn", "N");
            result.put("rtnMsg", "권한이 없습니다. 관리자만 사용할 수 있습니다.");
            return result;
        }
        
        // 사용자 정보를 변환하여 저장
        if (erp105005VO.getProcessUser() != null) {
            erp105005VO.setProcessUser(formatUserInfo(erp105005VO.getProcessUser()));
        }
        
        Map<String, Object> result = new HashMap<>();
        try {
            int processedCount = erp105005Service.erp105005RevertOutboundComplete(erp105005VO);
            result.put("rtnYn", "Y");
            result.put("rtnMsg", "반출완료가 되돌려졌습니다.");
            result.put("processedCount", processedCount);
            log.info("반출완료 되돌리기 성공: {}건 처리", processedCount);
        } catch (Exception e) {
            result.put("rtnYn", "N");
            result.put("rtnMsg", "처리 중 오류가 발생했습니다.");
            log.error("반출완료 되돌리기 실패", e);
        }
        
        return result;
    }
}

