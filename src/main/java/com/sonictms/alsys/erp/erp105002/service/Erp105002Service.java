package com.sonictms.alsys.erp.erp105002.service;

import com.sonictms.alsys.erp.erp105002.entity.Erp105002InboundVO;
import com.sonictms.alsys.erp.erp105002.entity.Erp105002ProductVO;
import com.sonictms.alsys.erp.erp105002.mapper.Erp105002Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class Erp105002Service {

    private final Erp105002Mapper erp105002Mapper;

    // 입고 가능한 상품 목록 조회
    public List<Erp105002ProductVO> getAvailableProducts(Erp105002ProductVO productVO) {
        return erp105002Mapper.getAvailableProducts(productVO);
    }

    // 입고 내역 목록 조회 (페이징)
    public List<Erp105002InboundVO> getInboundList(Erp105002InboundVO inboundVO) {
        // 총 개수 조회
        int totalCount = erp105002Mapper.getInboundListCount(inboundVO);
        inboundVO.setTotalCount(totalCount);
        
        // 총 페이지 수 계산
        int totalPages = (int) Math.ceil((double) totalCount / inboundVO.getPageSize());
        inboundVO.setTotalPages(totalPages);
        
        // 페이징된 목록 조회
        return erp105002Mapper.getInboundList(inboundVO);
    }

    // 입고 등록
    @Transactional
    public int insertInbound(Erp105002InboundVO inboundVO) {
        // 상품-화주사 매핑 검증
        validateProductAgencyMapping(inboundVO);
        return erp105002Mapper.insertInbound(inboundVO);
    }

    // 다중 상품 입고 등록
    @Transactional(rollbackFor = Exception.class)
    public int insertMultipleInbound(List<Erp105002InboundVO> inboundList) {
        // 사전 검증: 모든 항목을 먼저 검증
        for (Erp105002InboundVO inboundVO : inboundList) {
            validateProductAgencyMapping(inboundVO);
        }
        
        // 모든 검증이 통과하면 일괄 등록
        int result = 0;
        for (Erp105002InboundVO inboundVO : inboundList) {
            result += erp105002Mapper.insertInbound(inboundVO);
        }
        return result;
    }

    // 입고 완료 처리
    @Transactional
    public int completeInbound(Erp105002InboundVO inboundVO) {
        return erp105002Mapper.completeInbound(inboundVO);
    }

    // 입고 내역 수정
    @Transactional
    public int updateInbound(Erp105002InboundVO inboundVO) {
        return erp105002Mapper.updateInbound(inboundVO);
    }

    // 입고 내역 삭제 (Soft Delete)
    @Transactional
    public int deleteInbound(Erp105002InboundVO inboundVO) {
        // 완료된 입고는 삭제할 수 없음
        if ("COMPLETED".equals(inboundVO.getInboundStatus())) {
            throw new IllegalArgumentException("완료된 입고는 삭제할 수 없습니다.");
        }
        return erp105002Mapper.deleteInbound(inboundVO);
    }

    // 중복 입고 검증
    public boolean checkDuplicateInbound(Erp105002InboundVO inboundVO) {
        List<Erp105002InboundVO> existingInbounds = erp105002Mapper.checkDuplicateInbound(inboundVO);
        return existingInbounds.size() > 0;
    }
    
    // 상품-화주사 매핑 검증
    private void validateProductAgencyMapping(Erp105002InboundVO inboundVO) {
        if (inboundVO.getMtrlCd() == null || inboundVO.getMtrlCd().trim().isEmpty()) {
            throw new IllegalArgumentException("상품코드가 없습니다.");
        }
        if (inboundVO.getAgntCd() == null || inboundVO.getAgntCd().trim().isEmpty()) {
            throw new IllegalArgumentException("화주사코드가 없습니다.");
        }
        
        // 상품이 해당 화주사에 등록되어 있고, 입고 가능한 상품인지 확인
        // (MTO_YN='N' AND USE_YN='Y' 조건 포함)
        Erp105002ProductVO productVO = new Erp105002ProductVO();
        productVO.setCmpyCd(inboundVO.getCmpyCd());
        productVO.setAgntCd(inboundVO.getAgntCd());
        productVO.setMtrlCd(inboundVO.getMtrlCd());
        
        List<Erp105002ProductVO> availableProducts = erp105002Mapper.getAvailableProducts(productVO);
        if (availableProducts.isEmpty()) {
            throw new IllegalArgumentException(
                String.format(
                    "상품코드 '%s'는 화주사 '%s'에 등록되지 않았거나 입고 불가능한 상품입니다. (MTO 상품이거나 사용 중지된 상품)", 
                    inboundVO.getMtrlCd(), inboundVO.getAgntCd()
                )
            );
        }
        
        // 추가 검증: 상품이 실제로 입고 가능한 상태인지 확인
        Erp105002ProductVO foundProduct = availableProducts.get(0);
        if (!"Y".equals(foundProduct.getUseYn())) {
            throw new IllegalArgumentException(
                String.format("상품코드 '%s'는 사용 중지된 상품입니다. (USE_YN='%s')", 
                    inboundVO.getMtrlCd(), foundProduct.getUseYn())
            );
        }
        if (!"N".equals(foundProduct.getMtoYn())) {
            throw new IllegalArgumentException(
                String.format("상품코드 '%s'는 MTO(주문생산) 상품으로 입고 등록이 불가능합니다. (MTO_YN='%s')", 
                    inboundVO.getMtrlCd(), foundProduct.getMtoYn())
            );
        }
        
        // tblMtrlMId가 null인 경우 찾은 상품의 ID로 설정
        if (inboundVO.getTblMtrlMId() == null) {
            inboundVO.setTblMtrlMId(foundProduct.getTblMtrlMId());
        }
    }
}
