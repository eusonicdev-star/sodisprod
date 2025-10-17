package com.sonictms.alsys.erp.erp105002.controller;

import com.sonictms.alsys.erp.erp105002.entity.Erp105002InboundVO;
import com.sonictms.alsys.erp.erp105002.entity.Erp105002ProductVO;
import com.sonictms.alsys.erp.erp105002.service.Erp105002Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Transactional
@Controller
public class Erp105002Controller {

    private final Erp105002Service erp105002Service;

    // 입고 관리 화면 열기
    @GetMapping(value = {"erp105002"})
    public ModelAndView getErp105002(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105002/erp105002");
        return modelAndView;
    }

    // 입고 가능한 상품 목록 조회
    @PostMapping(value = {"erp105002AvailableProducts"})
    @ResponseBody
    public List<Erp105002ProductVO> getAvailableProducts(@RequestBody Erp105002ProductVO productVO) {
        return erp105002Service.getAvailableProducts(productVO);
    }

    // 입고 내역 목록 조회 (페이징)
    @PostMapping(value = {"erp105002InboundList"})
    @ResponseBody
    public Map<String, Object> getInboundList(@RequestBody Erp105002InboundVO inboundVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Erp105002InboundVO> inboundList = erp105002Service.getInboundList(inboundVO);
            result.put("success", true);
            result.put("data", inboundList);
            result.put("totalCount", inboundVO.getTotalCount());
            result.put("totalPages", inboundVO.getTotalPages());
            result.put("currentPage", inboundVO.getPageNum());
            result.put("pageSize", inboundVO.getPageSize());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 내역 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 입고 등록
    @PostMapping(value = {"erp105002InsertInbound"})
    @ResponseBody
    public Map<String, Object> insertInbound(@RequestBody Erp105002InboundVO inboundVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105002Service.insertInbound(inboundVO);
            result.put("success", true);
            result.put("message", "입고 등록이 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 다중 상품 입고 등록
    @PostMapping(value = {"erp105002InsertMultipleInbound"})
    @ResponseBody
    public Map<String, Object> insertMultipleInbound(@RequestBody List<Erp105002InboundVO> inboundList) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105002Service.insertMultipleInbound(inboundList);
            result.put("success", true);
            result.put("message", count + "건의 입고 등록이 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 등록 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 입고 완료 처리
    @PostMapping(value = {"erp105002CompleteInbound"})
    @ResponseBody
    public Map<String, Object> completeInbound(@RequestBody Erp105002InboundVO inboundVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105002Service.completeInbound(inboundVO);
            result.put("success", true);
            result.put("message", "입고 완료 처리가 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 완료 처리 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 입고 내역 수정
    @PostMapping(value = {"erp105002UpdateInbound"})
    @ResponseBody
    public Map<String, Object> updateInbound(@RequestBody Erp105002InboundVO inboundVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105002Service.updateInbound(inboundVO);
            result.put("success", true);
            result.put("message", "입고 내역 수정이 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 내역 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 입고 내역 삭제 (Soft Delete)
    @PostMapping(value = {"erp105002DeleteInbound"})
    @ResponseBody
    public Map<String, Object> deleteInbound(@RequestBody Erp105002InboundVO inboundVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105002Service.deleteInbound(inboundVO);
            result.put("success", true);
            result.put("message", "입고 내역 삭제가 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "입고 내역 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 엑셀 업로드 처리
    @PostMapping(value = {"erp105002ExcelUpload"})
    @ResponseBody
    public Map<String, Object> excelUpload(@RequestBody List<Erp105002InboundVO> inboundList) {
        Map<String, Object> result = new HashMap<>();
        try {
            int uploadedCount = erp105002Service.insertMultipleInbound(inboundList);
            result.put("success", true);
            result.put("message", uploadedCount + "건의 입고가 등록되었습니다.");
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "엑셀 업로드 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 중복 검증을 위한 입고 내역 조회
    @PostMapping(value = {"erp105002CheckDuplicate"})
    @ResponseBody
    public Map<String, Object> checkDuplicate(@RequestBody Erp105002InboundVO inboundVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean isDuplicate = erp105002Service.checkDuplicateInbound(inboundVO);
            result.put("success", true);
            result.put("isDuplicate", isDuplicate);
            if (isDuplicate) {
                result.put("message", "같은 예정일에 같은 상품이 이미 등록되어 있습니다.");
            } else {
                result.put("message", "등록 가능합니다.");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "중복 검증 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }
}
