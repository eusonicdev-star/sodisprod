package com.sonictms.alsys.erp.erp105004.controller;

import com.sonictms.alsys.erp.erp105004.entity.Erp105004AdjustmentVO;
import com.sonictms.alsys.erp.erp105004.entity.Erp105004InventoryVO;
import com.sonictms.alsys.erp.erp105004.service.Erp105004Service;
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
public class Erp105004Controller {

    private final Erp105004Service erp105004Service;

    // 재고 조정 화면 열기
    @GetMapping(value = {"erp105004"})
    public ModelAndView getErp105004(ModelAndView modelAndView) {
        modelAndView.setViewName("erp/erp105004/erp105004");
        return modelAndView;
    }

    // 현재 재고 조회
    @PostMapping(value = {"erp105004GetCurrentInventory"})
    @ResponseBody
    public Map<String, Object> getCurrentInventory(@RequestBody Erp105004InventoryVO inventoryVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            Erp105004InventoryVO currentInventory = erp105004Service.getCurrentInventory(inventoryVO);
            result.put("success", true);
            result.put("data", currentInventory);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "현재 재고 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 재고 조정 등록
    @PostMapping(value = {"erp105004InsertAdjustment"})
    @ResponseBody
    public Map<String, Object> insertAdjustment(@RequestBody Erp105004AdjustmentVO adjustmentVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105004Service.insertAdjustment(adjustmentVO);
            result.put("success", true);
            result.put("message", "재고 조정이 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "재고 조정 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 조정 내역 목록 조회 (페이징)
    @PostMapping(value = {"erp105004AdjustmentList"})
    @ResponseBody
    public Map<String, Object> getAdjustmentList(@RequestBody Erp105004AdjustmentVO adjustmentVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Erp105004AdjustmentVO> adjustmentList = erp105004Service.getAdjustmentList(adjustmentVO);
            result.put("success", true);
            result.put("data", adjustmentList);
            result.put("totalCount", adjustmentVO.getTotalCount());
            result.put("totalPages", adjustmentVO.getTotalPages());
            result.put("currentPage", adjustmentVO.getPageNum());
            result.put("pageSize", adjustmentVO.getPageSize());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "조정 내역 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 재고 현황 목록 조회 (페이징)
    @PostMapping(value = {"erp105004InventoryList"})
    @ResponseBody
    public Map<String, Object> getInventoryList(@RequestBody Erp105004InventoryVO inventoryVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Erp105004InventoryVO> inventoryList = erp105004Service.getInventoryList(inventoryVO);
            result.put("success", true);
            result.put("data", inventoryList);
            result.put("totalCount", inventoryVO.getTotalCount());
            result.put("totalPages", inventoryVO.getTotalPages());
            result.put("currentPage", inventoryVO.getPageNum());
            result.put("pageSize", inventoryVO.getPageSize());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "재고 현황 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 조정 내역 수정
    @PostMapping(value = {"erp105004UpdateAdjustment"})
    @ResponseBody
    public Map<String, Object> updateAdjustment(@RequestBody Erp105004AdjustmentVO adjustmentVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105004Service.updateAdjustment(adjustmentVO);
            result.put("success", true);
            result.put("message", "조정 내역 수정이 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "조정 내역 수정 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 조정 내역 삭제 (Soft Delete)
    @PostMapping(value = {"erp105004DeleteAdjustment"})
    @ResponseBody
    public Map<String, Object> deleteAdjustment(@RequestBody Erp105004AdjustmentVO adjustmentVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            int count = erp105004Service.deleteAdjustment(adjustmentVO);
            result.put("success", true);
            result.put("message", "조정 내역 삭제가 완료되었습니다.");
            result.put("count", count);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "조정 내역 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }

    // 재고 변동 이력 조회
    @PostMapping(value = {"erp105004GetInventoryHistory"})
    @ResponseBody
    public Map<String, Object> getInventoryHistory(@RequestBody Erp105004InventoryVO inventoryVO) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<Erp105004AdjustmentVO> historyList = erp105004Service.getInventoryHistory(inventoryVO);
            result.put("success", true);
            result.put("data", historyList);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "재고 변동 이력 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
        return result;
    }
}
