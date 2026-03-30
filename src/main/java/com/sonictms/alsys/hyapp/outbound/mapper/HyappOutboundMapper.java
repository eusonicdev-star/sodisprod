package com.sonictms.alsys.hyapp.outbound.mapper;

import com.sonictms.alsys.hyapp.outbound.entity.HyappOutboundVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HyappOutboundMapper {

    /**
     * 출고 검수 마스터 리스트 조회
     */
    List<HyappOutboundVO> selectInspectionMasterList(HyappOutboundVO vo);

    /**
     * 출고 검수 상세 리스트 조회
     */
    List<HyappOutboundVO> selectInspectionDetailList(HyappOutboundVO vo);

    /**
     * 스캔 시 해당 바코드 상품이 검수 대상인지 확인
     */
    HyappOutboundVO checkScanItem(HyappOutboundVO vo);

    /**
     * 주문 상세(TBL_SO_P)의 스캔 수량 1개 증가
     */
    int updateScanQty(Long tblSoPId);

    /**
     * 상품이 보관된 모든 피킹 가능 로케이션 재고 조회 (팝업용)
     */
    List<HyappOutboundVO> selectAllAvailableStocks(HyappOutboundVO vo);

    /**
     * 특정 STOCK_ID를 지정하여 재고 1개 차감
     */
    int updateStockDecrementById(HyappOutboundVO vo);

    /**
     * 특정 재고(STOCK_ID)의 현재 잔량 조회
     */
    int getRemainingStock(@Param("stockId") Long stockId);

    /**
     * 보관랙(PICKING_YN = 'N')에 있는 상품 재고 목록 조회 (보충용)
     */
    List<HyappOutboundVO> selectStorageStocks(HyappOutboundVO vo);

    /**
     * 재고 보충 처리 (피킹랙 재고 합산 또는 신규 생성)
     */
    int upsertStock(HyappOutboundVO vo);

    /**
     * 특정 수량(moveQty)만큼 재고 차감 (팔렛트 단위 이동용)
     */
    int updateStockDecrementByQty(HyappOutboundVO vo);

    HyappOutboundVO getStockLocationInfo(Long stockId);

    int insertNotice(Map<String, Object> noticeMap);

    /**
     * 보관랙(PICKING_YN = 'N')에 있는 상품 재고 목록 조회 (보충용)
     */
    List<HyappOutboundVO> getOutCancleList(HyappOutboundVO vo);

    // 재입고 확인 상태 업데이트
    int updateReturnConfirm(HyappOutboundVO vo);

    // 마스터 재고 수량 동기화 (앞서 만든 쿼리 호출용)
    int updateMaterialMasterStock(HyappOutboundVO vo);

}