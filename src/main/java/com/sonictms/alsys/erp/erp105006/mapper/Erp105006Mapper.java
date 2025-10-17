package com.sonictms.alsys.erp.erp105006.mapper;

import com.sonictms.alsys.erp.erp105006.entity.Erp105006VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105006Mapper {

    /**
     * 재입고 대기 목록 조회
     * @param erp105006VO 검색 조건
     * @return 재입고 대기 목록
     */
    List<Erp105006VO> erp105006List(Erp105006VO erp105006VO);

    /**
     * 재입고 완료 처리
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105006InboundComplete(Erp105006VO erp105006VO);

    /**
     * 반출 완료 처리
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105006OutboundComplete(Erp105006VO erp105006VO);

    /**
     * 일괄 재입고 완료 처리
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105006BatchInboundComplete(Erp105006VO erp105006VO);

    /**
     * 일괄 반출 완료 처리
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105006BatchOutboundComplete(Erp105006VO erp105006VO);

    /**
     * 반출 대상 여부 변경
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105006ToggleOutboundTarget(Erp105006VO erp105006VO);
}
