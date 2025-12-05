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

    /**
     * 입고완료 되돌리기 처리
     * @param erp105006VO 처리 정보
     * @return 처리된 행 수
     */
    int erp105006RevertInboundComplete(Erp105006VO erp105006VO);

    /**
     * 재입고 대상 상품 정보 조회 (MTO 여부 포함)
     * @param tblSoPId TBL_SO_P_ID
     * @return 상품 정보
     */
    Erp105006VO selectRestockProductInfo(String tblSoPId);

    /**
     * 재고 증가 (재입고)
     * @param erp105006VO 재고 정보
     * @return 처리된 행 수
     */
    int updateInventoryIncrease(Erp105006VO erp105006VO);

    /**
     * 재고 변동 이력 저장
     * @param erp105006VO 이력 정보
     * @return 처리된 행 수
     */
    int insertRestockInventoryHistory(Erp105006VO erp105006VO);

    /**
     * 재고 감소 (재입고 취소)
     * @param erp105006VO 재고 정보
     * @return 처리된 행 수
     */
    int updateInventoryDecrease(Erp105006VO erp105006VO);

    /**
     * 재고 변동 이력 저장 (재입고 취소)
     * @param erp105006VO 이력 정보
     * @return 처리된 행 수
     */
    int insertRestockRevertInventoryHistory(Erp105006VO erp105006VO);

    /**
     * 현재 재고 조회
     * @param erp105006VO 재고 조회 조건
     * @return 재고 정보
     */
    Erp105006VO selectInventory(Erp105006VO erp105006VO);
}
