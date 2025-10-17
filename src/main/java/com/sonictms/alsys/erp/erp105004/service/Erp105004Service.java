package com.sonictms.alsys.erp.erp105004.service;

import com.sonictms.alsys.erp.erp105004.entity.Erp105004AdjustmentVO;
import com.sonictms.alsys.erp.erp105004.entity.Erp105004InventoryVO;
import com.sonictms.alsys.erp.erp105004.mapper.Erp105004Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp105004Service {

    private final Erp105004Mapper erp105004Mapper;

    // 현재 재고 조회
    public Erp105004InventoryVO getCurrentInventory(Erp105004InventoryVO inventoryVO) {
        return erp105004Mapper.getCurrentInventory(inventoryVO);
    }

    // 재고 조정 등록
    @Transactional
    public int insertAdjustment(Erp105004AdjustmentVO adjustmentVO) {
        // 재고 조정 유효성 검사
        validateAdjustment(adjustmentVO);
        
        // 재고 조정 등록
        int result = erp105004Mapper.insertAdjustment(adjustmentVO);
        
        // 재고 현황 업데이트
        updateInventoryAfterAdjustment(adjustmentVO);
        
        return result;
    }

    // 조정 내역 목록 조회 (페이징)
    public List<Erp105004AdjustmentVO> getAdjustmentList(Erp105004AdjustmentVO adjustmentVO) {
        // 총 개수 조회
        int totalCount = erp105004Mapper.getAdjustmentListCount(adjustmentVO);
        adjustmentVO.setTotalCount(totalCount);
        
        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / adjustmentVO.getPageSize());
        adjustmentVO.setTotalPages(totalPages);
        
        // 페이징된 목록 조회
        return erp105004Mapper.getAdjustmentList(adjustmentVO);
    }

    // 재고 현황 목록 조회 (페이징)
    public List<Erp105004InventoryVO> getInventoryList(Erp105004InventoryVO inventoryVO) {
        // 총 개수 조회
        int totalCount = erp105004Mapper.getInventoryListCount(inventoryVO);
        inventoryVO.setTotalCount(totalCount);
        
        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / inventoryVO.getPageSize());
        inventoryVO.setTotalPages(totalPages);
        
        // 페이징된 목록 조회
        return erp105004Mapper.getInventoryList(inventoryVO);
    }

    // 조정 내역 수정
    @Transactional
    public int updateAdjustment(Erp105004AdjustmentVO adjustmentVO) {
        return erp105004Mapper.updateAdjustment(adjustmentVO);
    }

    // 조정 내역 삭제 (Soft Delete)
    @Transactional
    public int deleteAdjustment(Erp105004AdjustmentVO adjustmentVO) {
        return erp105004Mapper.deleteAdjustment(adjustmentVO);
    }

    // 재고 조정 유효성 검사
    private void validateAdjustment(Erp105004AdjustmentVO adjustmentVO) {
        if (adjustmentVO.getMtrlCd() == null || adjustmentVO.getMtrlCd().trim().isEmpty()) {
            throw new IllegalArgumentException("상품코드가 없습니다.");
        }
        if (adjustmentVO.getAgntCd() == null || adjustmentVO.getAgntCd().trim().isEmpty()) {
            throw new IllegalArgumentException("화주사코드가 없습니다.");
        }
        if (adjustmentVO.getAdjustmentType() == null || adjustmentVO.getAdjustmentType().trim().isEmpty()) {
            throw new IllegalArgumentException("조정 유형을 선택해주세요.");
        }
        if (adjustmentVO.getAdjustmentQty() == null || adjustmentVO.getAdjustmentQty() <= 0) {
            throw new IllegalArgumentException("조정 수량을 입력해주세요.");
        }
        if (adjustmentVO.getAdjustmentReason() == null || adjustmentVO.getAdjustmentReason().trim().isEmpty()) {
            throw new IllegalArgumentException("조정 사유를 입력해주세요.");
        }
        if (adjustmentVO.getAdjustmentDate() == null) {
            throw new IllegalArgumentException("조정 일자를 선택해주세요.");
        }

        // 현재 재고 조회
        Erp105004InventoryVO searchVO = new Erp105004InventoryVO();
        searchVO.setCmpyCd(adjustmentVO.getCmpyCd());
        searchVO.setAgntCd(adjustmentVO.getAgntCd());
        searchVO.setMtrlCd(adjustmentVO.getMtrlCd());
        
        Erp105004InventoryVO currentInventory = erp105004Mapper.getCurrentInventory(searchVO);
        if (currentInventory == null) {
            throw new IllegalArgumentException("해당 상품의 재고 정보를 찾을 수 없습니다.");
        }

        // 조정 후 수량 계산 및 검증
        int afterQty = 0;
        if ("INCREASE".equals(adjustmentVO.getAdjustmentType())) {
            afterQty = currentInventory.getCurrentQty() + adjustmentVO.getAdjustmentQty();
        } else if ("DECREASE".equals(adjustmentVO.getAdjustmentType())) {
            afterQty = currentInventory.getCurrentQty() - adjustmentVO.getAdjustmentQty();
            if (afterQty < 0) {
                throw new IllegalArgumentException("조정 후 수량이 음수가 될 수 없습니다.");
            }
        } else {
            throw new IllegalArgumentException("올바르지 않은 조정 유형입니다.");
        }

        // 조정 전/후 수량 설정
        adjustmentVO.setBeforeQty(currentInventory.getCurrentQty());
        adjustmentVO.setAfterQty(afterQty);
    }

    // 재고 변동 이력 조회
    public List<Erp105004AdjustmentVO> getInventoryHistory(Erp105004InventoryVO inventoryVO) {
        return erp105004Mapper.getInventoryHistory(inventoryVO);
    }

    // 재고 조정 후 재고 현황 업데이트
    private void updateInventoryAfterAdjustment(Erp105004AdjustmentVO adjustmentVO) {
        Erp105004InventoryVO inventoryVO = new Erp105004InventoryVO();
        inventoryVO.setCmpyCd(adjustmentVO.getCmpyCd());
        inventoryVO.setAgntCd(adjustmentVO.getAgntCd());
        inventoryVO.setAgntNm(adjustmentVO.getAgntNm());
        inventoryVO.setTblMtrlMId(adjustmentVO.getTblMtrlMId());
        inventoryVO.setMtrlCd(adjustmentVO.getMtrlCd());
        inventoryVO.setMtrlNm(adjustmentVO.getMtrlNm());
        inventoryVO.setCurrentQty(adjustmentVO.getAfterQty());
        inventoryVO.setUpdtUser(adjustmentVO.getUpdtUser());

        // 기존 재고 현황이 있는지 확인
        Erp105004InventoryVO existingInventory = erp105004Mapper.getCurrentInventory(inventoryVO);
        
        if (existingInventory != null) {
            // 기존 재고 현황 업데이트
            inventoryVO.setTblInvProductId(existingInventory.getTblInvProductId());
            erp105004Mapper.updateInventory(inventoryVO);
        } else {
            // 새로운 재고 현황 등록
            inventoryVO.setSaveUser(adjustmentVO.getSaveUser());
            erp105004Mapper.insertInventory(inventoryVO);
        }
    }
}
