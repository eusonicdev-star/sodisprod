package com.sonictms.alsys.erp.erp105003.mapper;

import com.sonictms.alsys.erp.erp105003.entity.Erp105003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 입고 완료 처리 Mapper
 */
@Mapper
public interface Erp105003Mapper {
    
    /**
     * 입고 예정 목록 조회 (PENDING 상태만)
     * @param vo 검색 조건
     * @return 입고 예정 목록
     */
    List<Erp105003VO> getPendingInboundList(Erp105003VO vo);
    int getPendingInboundListCount(Erp105003VO vo);
    
    /**
     * 입고 완료 처리
     * @param vo 입고 완료 정보
     * @return 처리 결과
     */
    int completeInbound(Erp105003VO vo);
    
    /**
     * 입고 완료 처리 (일괄)
     * @param voList 입고 완료 정보 목록
     * @return 처리 결과
     */
    int completeMultipleInbound(List<Erp105003VO> voList);
    
    /**
     * 재고 수량 업데이트 (입고 완료 시)
     * @param vo 입고 완료 정보
     * @return 처리 결과
     */
    int updateInventoryQuantity(Erp105003VO vo);
    
    /**
     * 재고 수량 업데이트 (일괄 입고 완료 시)
     * @param voList 입고 완료 정보 목록
     * @return 처리 결과
     */
    int updateMultipleInventoryQuantity(List<Erp105003VO> voList);
    
    /**
     * 입고 내역 조회 (모든 상태 포함)
     * @param vo 검색 조건
     * @return 입고 내역 목록
     */
    List<Erp105003VO> getInboundHistoryList(Erp105003VO vo);
    int getInboundHistoryListCount(Erp105003VO vo);
    
    /**
     * 입고 되돌리기 (일괄)
     * @param voList 입고 되돌리기 정보 목록
     * @return 처리 결과
     */
    int revertInbound(List<Erp105003VO> voList);
    
    /**
     * 재고 수량 차감 (입고 되돌리기 시)
     * @param voList 입고 되돌리기 정보 목록
     * @return 처리 결과
     */
    int revertInventoryQuantity(List<Erp105003VO> voList);
}
