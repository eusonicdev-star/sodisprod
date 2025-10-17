package com.sonictms.alsys.erp.erp105005.mapper;

import com.sonictms.alsys.erp.erp105005.entity.Erp105005VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105005Mapper {

    /**
     * 교환/반품 재입고 목록 조회
     * @param erp105005VO 검색 조건
     * @return 재입고 목록
     */
    List<Erp105005VO> erp105005List(Erp105005VO erp105005VO);

    /**
     * 재입고 완료 처리
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105005InboundComplete(Erp105005VO erp105005VO);

    /**
     * 반출 완료 처리
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105005OutboundComplete(Erp105005VO erp105005VO);

    /**
     * 일괄 재입고 완료 처리
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105005BatchInboundComplete(Erp105005VO erp105005VO);

    /**
     * 일괄 반출 완료 처리
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105005BatchOutboundComplete(Erp105005VO erp105005VO);

    /**
     * 반출 대상 여부 변경
     * @param erp105005VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105005ToggleOutboundTarget(Erp105005VO erp105005VO);
}

