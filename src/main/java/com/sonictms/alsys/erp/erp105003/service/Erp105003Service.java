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
        
        System.out.println("입고 완료 처리 시작 - 상품: " + vo.getMtrlCd() + ", 수량: " + vo.getActualQuantity());
        
        // 입고 완료 처리
        int result = erp105003Mapper.completeInbound(vo);
        System.out.println("입고 완료 처리 결과: " + result + "건");
        
        // 재고 수량 업데이트
        if (result > 0) {
            System.out.println("재고 업데이트 시작 - 회사: " + vo.getCmpyCd() + ", 화주: " + vo.getAgntCd() + ", 상품: " + vo.getMtrlCd());
            int inventoryResult = erp105003Mapper.updateInventoryQuantity(vo);
            System.out.println("재고 업데이트 결과: " + inventoryResult + "건, 상품: " + vo.getMtrlCd());
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
        
        System.out.println("일괄 입고 완료 처리 시작 - 건수: " + voList.size());
        
        // 입고 완료 처리
        int result = erp105003Mapper.completeMultipleInbound(voList);
        System.out.println("일괄 입고 완료 처리 결과: " + result + "건");
        
        // 재고 수량 업데이트
        if (result > 0) {
            System.out.println("일괄 재고 업데이트 시작 - 건수: " + voList.size());
            int inventoryResult = erp105003Mapper.updateMultipleInventoryQuantity(voList);
            System.out.println("일괄 재고 업데이트 결과: " + inventoryResult + "건, 처리 건수: " + voList.size());
        }
        
        // 실제 처리된 건수는 요청된 건수와 동일하다고 가정
        // (단일 UPDATE 문으로 변경했으므로 정확한 건수가 반환됨)
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
}
