package com.sonictms.alsys.hyapp.inbound.mapper;

import com.sonictms.alsys.hyapp.inbound.dto.InboundProductSearchDTO;
import com.sonictms.alsys.hyapp.inbound.dto.ProductSearchReq;
import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HyappInboundMapper {

    List<HyappInboundVO> selectPendingInboundList(HyappInboundVO vo);

    List<HyappInboundVO> getHyappInboundList_barcode(HyappInboundVO vo);

    // ? 검수 저장(입고예정 1건 업데이트)
    int updateInboundQc(HyappInboundVO vo);

    /**
     * 입고 상품 검수 수량 누적 및 상태 업데이트
     * @param vo (actualQuantity, failQty, tblInboundProductId 등 포함)
     * @return 업데이트된 행의 수
     */
    int updateInboundProductQc(HyappInboundVO vo);

    /**
     * 특정 입고 상품의 현재 상태 및 누적 수량 조회
     * @param tblInboundProductId 입고 상품 PK
     * @return 입고 상품 정보 (inboundStatus 확인용)
     */
    HyappInboundVO selectInboundProductStatus(Long tblInboundProductId);

    // ? 재고 반영용 데이터 조회(필요 필드만)
    HyappInboundVO selectInboundForInventory(HyappInboundVO vo);

    // ? 재고 수량 업데이트 + 재고 변동 이력 기록 (일괄/단건 모두 가능)
    int updateMultipleInventoryQuantity(List<HyappInboundVO> list);

    // 검수완료(COMPLETE) 목록 조회(적치 화면에서 사용)
    List<HyappInboundVO> selectQcCompleteList(HyappInboundVO vo);

    List<HyappInboundVO> getWhZoneList(HyappInboundVO vo);

    HyappInboundVO selectLocByZoneId(
            @Param("cmpyCd") String cmpyCd,
            @Param("zoneId") String zoneId
    );

    // 적치 정보 저장
    int savePutOne(HyappInboundVO vo);

    // 적치 정보 저장
    int upsertStock(HyappInboundVO vo);

    // 적치 정보 저장
    void savePut(List<HyappInboundVO> list);

    HyappInboundVO selectLocInfoByBarcode(
            @Param("cmpyCd") String cmpyCd,
            @Param("locBarcode") String locBarcode
    );

    // 출력 요청 저장
    void insertPrintQueue(Map<String, Object> params);

    // HyappInboundMapper.java
    List<InboundProductSearchDTO> selectInboundProductList(ProductSearchReq req);

    int insertInboundProduct(HyappInboundVO vo);

    /**
     * 입차 차량 정보(Master) 저장
     * @param vo vhclTonType, inMethod, palletQty 등을 담은 객체
     * @return 성공 시 1 반환 (vo 객체 내의 tblInboundMasterId에 생성된 PK가 자동으로 담김)
     */
    int insertInboundMaster(HyappInboundVO vo);

    /**
     * TBL_INBOUND_MASTER 테이블에서 조건에 맞는 차량 리스트 조회
     * @param vo 검색 조건 객체
     * @return 조회된 차량 VO 리스트
     */
    List<HyappInboundVO> selectInboundMasterList(HyappInboundVO vo);

    // 검수완료(COMPLETE) 목록 조회(적치 화면에서 사용)
    List<HyappInboundVO> selectPutawayItemByBarcode(HyappInboundVO vo);

    // 고정 로케이션의 상세 정보(LOC_ID, ZONE_ID 등)를 가져오는 쿼리를 추가
    HyappInboundVO getFixedLocationInfo(@Param("mtrlCd") String mtrlCd, @Param("cmpyCd") String cmpyCd);

    // 고정 로케이션의 상세 정보(LOC_ID, ZONE_ID 등)를 가져오는 쿼리를 추가
    HyappInboundVO getScrapLocationInfo(@Param("cmpyCd") String cmpyCd);

    int updatePutawayStatusY(HyappInboundVO vo);
}
