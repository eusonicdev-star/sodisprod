package com.sonictms.alsys.erp.erp105002.mapper;

import com.sonictms.alsys.erp.erp105002.entity.Erp105002InboundVO;
import com.sonictms.alsys.erp.erp105002.entity.Erp105002ProductVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105002Mapper {

    // 입고 가능한 상품 목록 조회 (USE_YN='Y' AND MTO_YN='N')
    List<Erp105002ProductVO> getAvailableProducts(Erp105002ProductVO productVO);

    // 입고 내역 목록 조회 (페이징)
    List<Erp105002InboundVO> getInboundList(Erp105002InboundVO inboundVO);
    
    // 입고 내역 총 개수 조회 (페이징용)
    int getInboundListCount(Erp105002InboundVO inboundVO);

    // 입고 등록
    int insertInbound(Erp105002InboundVO inboundVO);

    // 입고 완료 처리
    int completeInbound(Erp105002InboundVO inboundVO);

    // 입고 내역 수정
    int updateInbound(Erp105002InboundVO inboundVO);

    // 입고 내역 삭제 (Soft Delete)
    int deleteInbound(Erp105002InboundVO inboundVO);

    // 중복 입고 검증 (같은 예정일 + 같은 상품코드)
    List<Erp105002InboundVO> checkDuplicateInbound(Erp105002InboundVO inboundVO);
    
    // 입고 완료 취소 처리
    int cancelInbound(Erp105002InboundVO inboundVO);
}
