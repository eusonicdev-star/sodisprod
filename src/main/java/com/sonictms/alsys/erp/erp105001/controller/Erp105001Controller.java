package com.sonictms.alsys.erp.erp105001.controller;

import com.sonictms.alsys.erp.erp105001.entity.Erp105001VO;
import com.sonictms.alsys.erp.erp105001.service.Erp105001Service;
import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
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
public class Erp105001Controller {

    private final Erp105001Service erp105001Service;

    // 재고 현황 조회 화면 열기
    @GetMapping(value = {"erp105001"})
    public ModelAndView getErp105001(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105001/erp105001");
        return modelAndView;
    }

    // 재고 현황 리스트 조회
    @RequestMapping(value = {"erp105001List"}, method = RequestMethod.POST)
    @ResponseBody
    public List<Erp105001VO> erp105001List(Erp105001VO erp105001VO, HttpServletRequest request) {
        log.info("재고 현황 조회 요청");
        log.info("검색 조건: {}", erp105001VO);
        
        // 세션에서 사용자 권한 정보 가져오기
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String agntCd = (String) request.getSession().getAttribute("agntCd");
        
        log.info("사용자 권한 정보 - userGrntCd: {}, agntCd: {}", userGrntCd, agntCd);
        
        // 시스템 관리자가 아닌 경우 본인 화주사로만 조회 강제
        if (!"9999".equals(userGrntCd)) {
            if (agntCd != null && !agntCd.isEmpty()) {
                log.info("일반 사용자 - 본인 화주사로만 조회 강제: {}", agntCd);
                // 요청된 화주사 코드를 세션의 화주사 코드로 강제 변경
                erp105001VO.setAgntCd(agntCd);
            } else {
                log.warn("일반 사용자인데 세션에 화주사 코드가 없습니다.");
            }
        }
        
        List<Erp105001VO> list = erp105001Service.erp105001List(erp105001VO);
        log.info("조회 결과: {}건", list != null ? list.size() : 0);
        
        return list;
    }

    // 재고 조정
    @PostMapping("erp105001Adjustment")
    @ResponseBody
    public Map<String, Object> erp105001Adjustment(@RequestBody Erp105001VO erp105001VO, HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 세션에서 사용자 권한 정보 가져오기
            String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
            String agntCd = (String) request.getSession().getAttribute("agntCd");
            
            log.info("재고 조정 요청 - userGrntCd: {}, 요청 화주사: {}", userGrntCd, erp105001VO.getAgntCd());
            
            // 시스템 관리자가 아닌 경우 본인 화주사 재고만 조정 가능
            if (!"9999".equals(userGrntCd)) {
                if (agntCd != null && !agntCd.isEmpty()) {
                    // 요청된 화주사 코드와 세션의 화주사 코드가 일치하는지 확인
                    if (!agntCd.equals(erp105001VO.getAgntCd())) {
                        log.warn("권한 없음 - 요청 화주사: {}, 세션 화주사: {}", erp105001VO.getAgntCd(), agntCd);
                        result.put("success", false);
                        result.put("message", "본인 화주사의 재고만 조정할 수 있습니다.");
                        return result;
                    }
                } else {
                    log.warn("일반 사용자인데 세션에 화주사 코드가 없습니다.");
                    result.put("success", false);
                    result.put("message", "화주사 정보가 없습니다.");
                    return result;
                }
            }
            
            boolean success = erp105001Service.adjustInventory(erp105001VO);
            result.put("success", success);
            if (success) {
                result.put("message", "재고 조정이 완료되었습니다.");
            } else {
                result.put("message", "재고 조정에 실패했습니다.");
            }
        } catch (Exception e) {
            log.error("재고 조정 중 오류 발생", e);
            result.put("success", false);
            result.put("message", "재고 조정 중 오류가 발생했습니다: " + e.getMessage());
        }

        return result;
    }

    // 출고 대기 주문 조회 (모달용)
    @PostMapping("erp105001OutboundWaitList")
    @ResponseBody
    public List<Erp105008VO> erp105001OutboundWaitList(@RequestBody Erp105001VO erp105001VO, HttpServletRequest request) {
        log.info("출고 대기 주문 조회 요청 - 상품코드: {}, 화주사코드: {}", erp105001VO.getProductCd(), erp105001VO.getAgntCd());
        
        // 세션에서 사용자 권한 정보 가져오기
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String agntCd = (String) request.getSession().getAttribute("agntCd");
        
        // 시스템 관리자가 아닌 경우 본인 화주사로만 조회 강제
        if (!"9999".equals(userGrntCd)) {
            if (agntCd != null && !agntCd.isEmpty()) {
                erp105001VO.setAgntCd(agntCd);
            }
        }
        
        // Erp105001Mapper의 모달 전용 쿼리 사용 (가용재고 계산과 동일한 로직)
        List<Erp105008VO> list = erp105001Service.selectOutboundWaitListForModal(erp105001VO);
        log.info("출고 대기 주문 조회 결과: {}건", list != null ? list.size() : 0);
        
        return list;
    }

    // 재고 변동 이력 조회 (모달용)
    @PostMapping("erp105001AdjustmentHistory")
    @ResponseBody
    public List<Erp105001VO> erp105001AdjustmentHistory(@RequestBody Erp105001VO erp105001VO, HttpServletRequest request) {
        log.info("재고 변동 이력 조회 요청 - 상품코드: {}, 화주사코드: {}", erp105001VO.getProductCd(), erp105001VO.getAgntCd());
        
        // 세션에서 사용자 권한 정보 가져오기
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String agntCd = (String) request.getSession().getAttribute("agntCd");
        
        // 시스템 관리자가 아닌 경우 본인 화주사로만 조회 강제
        if (!"9999".equals(userGrntCd)) {
            if (agntCd != null && !agntCd.isEmpty()) {
                erp105001VO.setAgntCd(agntCd);
            }
        }
        
        List<Erp105001VO> list = erp105001Service.selectAdjustmentHistory(erp105001VO);
        log.info("재고 변동 이력 조회 결과: {}건", list != null ? list.size() : 0);
        
        return list;
    }

    // 재고 변동 이력 조회 (모달용)
    @PostMapping("erp105001LocationStock")
    @ResponseBody
    public List<Erp105001VO> erp105001LocationStock(@RequestBody Erp105001VO erp105001VO, HttpServletRequest request) {
        log.info("재고 로케이션 조회 요청 - 상품코드: {}, 화주사코드: {}", erp105001VO.getProductCd(), erp105001VO.getAgntCd());

        // 세션에서 사용자 권한 정보 가져오기
        String userGrntCd = (String) request.getSession().getAttribute("userGrntCd");
        String agntCd = (String) request.getSession().getAttribute("agntCd");

        // 시스템 관리자가 아닌 경우 본인 화주사로만 조회 강제
        if (!"9999".equals(userGrntCd)) {
            if (agntCd != null && !agntCd.isEmpty()) {
                erp105001VO.setAgntCd(agntCd);
            }
        }

        List<Erp105001VO> list = erp105001Service.selectLocationStock(erp105001VO);
        log.info("재고 로케이션 조회 결과: {}건", list != null ? list.size() : 0);

        return list;
    }

}
