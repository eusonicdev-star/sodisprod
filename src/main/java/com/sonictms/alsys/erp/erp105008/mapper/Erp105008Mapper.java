package com.sonictms.alsys.erp.erp105008.mapper;

import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105008Mapper {

    /**
     * 출고대기 리스트 조회
     */
    List<Erp105008VO> selectOutboundWaitList(Erp105008VO vo);
    
    /**
     * 상품 정보 조회 (단건)
     */
    Erp105008VO selectProductInfo(Erp105008VO vo);
    
    /**
     * 재고 조회
     */
    Erp105008VO selectInventory(Erp105008VO vo);
    
    /**
     * 재고 차감
     */
    int updateInventoryDecrease(Erp105008VO vo);
    
    /**
     * 재고 변동 이력 기록
     */
    int insertInventoryHistory(Erp105008VO vo);
    
    /**
     * 출고 정보 업데이트 (택배)
     */
    int updateOutboundInfoCourier(Erp105008VO vo);
    
    /**
     * 출고 정보 업데이트 (업체직출)
     */
    int updateOutboundInfoDirect(Erp105008VO vo);
    
    /**
     * 주문 상태 변경
     */
    int updateOrderStatus(Erp105008VO vo);
}
