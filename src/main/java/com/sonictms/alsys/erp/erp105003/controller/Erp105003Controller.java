package com.sonictms.alsys.erp.erp105003.controller;

import com.sonictms.alsys.erp.erp105003.entity.Erp105003VO;
import com.sonictms.alsys.erp.erp105003.service.Erp105003Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 입고 완료 처리 Controller
 */
@Controller
public class Erp105003Controller {
    
    @Autowired
    private Erp105003Service erp105003Service;
    
    /**
     * 입고 완료 처리 페이지
     * @param model Model
     * @return 페이지 경로
     */
    @GetMapping("/erp105003")
    public String erp105003(Model model) {
        return "erp/erp105003/erp105003";
    }
    
    /**
     * 입고 예정 목록 조회
     * @param vo 검색 조건
     * @return 입고 예정 목록
     */
    @PostMapping("/erp105003PendingList")
    @ResponseBody
    public Map<String, Object> getPendingInboundList(@RequestBody Erp105003VO vo) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Erp105003VO> pendingInboundList = erp105003Service.getPendingInboundList(vo);
            result.put("success", true);
            result.put("data", pendingInboundList);
            result.put("totalCount", vo.getTotalCount());
            result.put("totalPages", vo.getTotalPages());
            result.put("currentPage", vo.getPageNum());
            result.put("pageSize", vo.getPageSize());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 예정 목록 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }
    
    /**
     * 입고 완료 처리
     * @param vo 입고 완료 정보
     * @return 처리 결과
     */
    @PostMapping("/erp105003CompleteInbound")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeInbound(@RequestBody Erp105003VO vo) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int updateCount = erp105003Service.completeInbound(vo);
            if (updateCount > 0) {
                result.put("success", true);
                result.put("message", "입고 완료 처리가 완료되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "입고 완료 처리에 실패했습니다. 이미 처리된 항목이거나 존재하지 않는 항목입니다.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 완료 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 입고 완료 처리 (일괄)
     * @param voList 입고 완료 정보 목록
     * @return 처리 결과
     */
    @PostMapping("/erp105003CompleteMultipleInbound")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeMultipleInbound(@RequestBody List<Erp105003VO> voList) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int updateCount = erp105003Service.completeMultipleInbound(voList);
            if (updateCount > 0) {
                result.put("success", true);
                result.put("message", updateCount + "건의 입고 완료 처리가 완료되었습니다.");
            } else {
                result.put("success", false);
                result.put("message", "입고 완료 처리에 실패했습니다. 이미 처리된 항목이거나 존재하지 않는 항목입니다.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 완료 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        
        return ResponseEntity.ok(result);
    }
    
    /**
     * 입고 내역 조회 (모든 상태 포함)
     * @param vo 검색 조건
     * @return 입고 내역 목록
     */
    @PostMapping("/erp105003InboundHistory")
    @ResponseBody
    public Map<String, Object> getInboundHistoryList(@RequestBody Erp105003VO vo) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Erp105003VO> inboundHistoryList = erp105003Service.getInboundHistoryList(vo);
            result.put("success", true);
            result.put("data", inboundHistoryList);
            result.put("totalCount", vo.getTotalCount());
            result.put("totalPages", vo.getTotalPages());
            result.put("currentPage", vo.getPageNum());
            result.put("pageSize", vo.getPageSize());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 완료 내역 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }
}
