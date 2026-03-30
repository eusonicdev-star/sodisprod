package com.sonictms.alsys.erp.erp105010.mapper;

import com.sonictms.alsys.erp.erp105010.entity.Erp105010VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105010Mapper {
    
    /**
     * 택배/직출 리스트 조회 (MTO 포함 모든 건)
     */
    List<Erp105010VO> selectCourierDirectList(Erp105010VO vo);
    
    /**
     * 출고 대상 상품 정보 조회
     */
    Erp105010VO selectProductInfo(Long tblSoPId);
    
    /**
     * AL 오더와 상품코드로 상품 정보 조회 (엑셀 업로드용)
     */
    Erp105010VO selectProductInfoBySoNoAndProdCd(Erp105010VO vo);
    
    /**
     * 재고 조회
     */
    Erp105010VO selectInventory(Erp105010VO vo);
    
    /**
     * 재고 차감 (MTO가 아닌 경우만)
     */
    int updateInventoryDecrease(Erp105010VO vo);
    
    /**
     * 재고 변동 이력 저장
     */
    int insertInventoryHistory(Erp105010VO vo);
    
    /**
     * 택배 출고 정보 업데이트
     */
    int updateOutboundInfoCourier(Erp105010VO vo);
    
    /**
     * 업체직출 출고 정보 업데이트
     */
    int updateOutboundInfoDirect(Erp105010VO vo);
    
    /**
     * 주문 상태 변경 (배송완료: 9999)
     */
    int updateOrderStatus(Long tblSoMId);
}
