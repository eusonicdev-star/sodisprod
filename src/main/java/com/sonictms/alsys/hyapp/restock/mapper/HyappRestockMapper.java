package com.sonictms.alsys.hyapp.restock.mapper;

import com.sonictms.alsys.hyapp.restock.entity.HyappRestockVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface HyappRestockMapper {
    // [추가] SO_NO(주문번호)로 미입고 품목 리스트 조회
    List<HyappRestockVO> selectTargetItemsBySoNo(String soNo);

    int updateRestockComplete(HyappRestockVO vo);

    List<HyappRestockVO> selectHyappRestockPutList(@Param("cmpyCd") String cmpyCd, @Param("dcCd") String dcCd);

    int updatePutawayStatus(@Param("tblRestockWaitId") String waitId, @Param("saveUser") String saveUser);

    void updateLocationStockIncrement(Map<String, Object> params);

    // [추가] 불량/반품 입고 대기 목록 조회
    List<HyappRestockVO> selectReturnInboundWaitList(HyappRestockVO vo);
}