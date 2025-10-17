package com.sonictms.alsys.erp.erp105004.mapper;

import com.sonictms.alsys.erp.erp105004.entity.Erp105004AdjustmentVO;
import com.sonictms.alsys.erp.erp105004.entity.Erp105004InventoryVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105004Mapper {

    // 현재 재고 조회
    Erp105004InventoryVO getCurrentInventory(Erp105004InventoryVO inventoryVO);

    // 재고 조정 등록
    int insertAdjustment(Erp105004AdjustmentVO adjustmentVO);

    // 조정 내역 목록 조회 (페이징)
    List<Erp105004AdjustmentVO> getAdjustmentList(Erp105004AdjustmentVO adjustmentVO);
    
    // 조정 내역 총 개수 조회 (페이징용)
    int getAdjustmentListCount(Erp105004AdjustmentVO adjustmentVO);

    // 재고 현황 목록 조회 (페이징)
    List<Erp105004InventoryVO> getInventoryList(Erp105004InventoryVO inventoryVO);
    
    // 재고 현황 총 개수 조회 (페이징용)
    int getInventoryListCount(Erp105004InventoryVO inventoryVO);

    // 조정 내역 수정
    int updateAdjustment(Erp105004AdjustmentVO adjustmentVO);

    // 조정 내역 삭제 (Soft Delete)
    int deleteAdjustment(Erp105004AdjustmentVO adjustmentVO);

    // 재고 현황 등록
    int insertInventory(Erp105004InventoryVO inventoryVO);

    // 재고 현황 업데이트
    int updateInventory(Erp105004InventoryVO inventoryVO);

    // 재고 변동 이력 조회
    List<Erp105004AdjustmentVO> getInventoryHistory(Erp105004InventoryVO inventoryVO);
}
