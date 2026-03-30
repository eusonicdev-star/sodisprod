package com.sonictms.alsys.erp.erp105003.service;

import com.sonictms.alsys.erp.erp105003.entity.Erp105003VO;
import com.sonictms.alsys.erp.erp105003.mapper.Erp105003Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 입고 완료 처리 Service
 */
@Service
public class Erp105003Service {
    
    @Autowired
    private Erp105003Mapper erp105003Mapper;
    
    /**
     * 입고 예정 목록 조회
     * @param vo 검색 조건
     * @return 입고 예정 목록
     */
    public List<Erp105003VO> getPendingInboundList(Erp105003VO vo) {
        int totalCount = erp105003Mapper.getPendingInboundListCount(vo);
        vo.setTotalCount(totalCount);
        int totalPages = (int) Math.ceil((double) totalCount / vo.getPageSize());
        vo.setTotalPages(totalPages);
        return erp105003Mapper.getPendingInboundList(vo);
    }
    
    /**
     * 입고 완료 처리
     * @param vo 입고 완료 정보
     * @return 처리 결과
     */
    @Transactional
    public int completeInbound(Erp105003VO vo) {
        // 유효성 검사
        if (vo.getActualQuantity() == null || vo.getActualQuantity() <= 0) {
            throw new IllegalArgumentException("실제 입고 수량을 입력해주세요.");
        }
        if (vo.getActualDate() == null || vo.getActualDate().trim().isEmpty()) {
            throw new IllegalArgumentException("실제 입고 날짜를 입력해주세요.");
        }
        
        // 입고 완료 처리
        int result = erp105003Mapper.completeInbound(vo);
        
        // 재고 수량 업데이트
        if (result > 0) {
            erp105003Mapper.updateInventoryQuantity(vo);
        }
        
        return result;
    }
    
    /**
     * 입고 완료 처리 (일괄)
     * @param voList 입고 완료 정보 목록
     * @return 처리 결과
     */
    @Transactional
    public int completeMultipleInbound(List<Erp105003VO> voList) {
        if (voList == null || voList.isEmpty()) {
            throw new IllegalArgumentException("처리할 항목이 없습니다.");
        }
        
        // 유효성 검사
        for (Erp105003VO vo : voList) {
            if (vo.getActualQuantity() == null || vo.getActualQuantity() <= 0) {
                throw new IllegalArgumentException("실제 입고 수량을 입력해주세요.");
            }
            if (vo.getActualDate() == null || vo.getActualDate().trim().isEmpty()) {
                throw new IllegalArgumentException("실제 입고 날짜를 입력해주세요.");
            }
        }
        
        // 입고 완료 처리
        int result = erp105003Mapper.completeMultipleInbound(voList);
        
        // 재고 수량 업데이트
        if (result > 0) {
            erp105003Mapper.updateMultipleInventoryQuantity(voList);
        }
        
        return result > 0 ? voList.size() : 0;
    }
    
    /**
     * 입고 내역 조회 (모든 상태 포함)
     * @param vo 검색 조건
     * @return 입고 내역 목록
     */
    public List<Erp105003VO> getInboundHistoryList(Erp105003VO vo) {
        int totalCount = erp105003Mapper.getInboundHistoryListCount(vo);
        vo.setTotalCount(totalCount);
        int totalPages = (int) Math.ceil((double) totalCount / vo.getPageSize());
        vo.setTotalPages(totalPages);
        return erp105003Mapper.getInboundHistoryList(vo);
    }
    
    /**
     * 입고 되돌리기 (일괄)
     * @param voList 입고 되돌리기 정보 목록
     * @return 처리 결과
     */
    @Transactional
    public int revertInbound(List<Erp105003VO> voList) {
        if (voList == null || voList.isEmpty()) {
            throw new IllegalArgumentException("되돌릴 항목이 없습니다.");
        }
        
        // 입고 상태를 PENDING으로 변경
        int result = erp105003Mapper.revertInbound(voList);
        
        // 재고 수량 차감
        if (result > 0) {
            erp105003Mapper.revertInventoryQuantity(voList);
        }
        
        return result > 0 ? voList.size() : 0;
    }
}
