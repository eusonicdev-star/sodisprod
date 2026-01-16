package com.sonictms.alsys.hyapp.inbound.mapper;

import com.sonictms.alsys.hyapp.inbound.entity.HyappInboundVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface HyappInboundMapper {
    List<HyappInboundVO> getHyappInboundList_barcode(HyappInboundVO vo);


    // ? 검수 저장(입고예정 1건 업데이트)
    int updateInboundQc(HyappInboundVO vo);

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
            @Param("zoneId") String zoneId,
            @Param("locBarcode") String locBarcode
    );
}
