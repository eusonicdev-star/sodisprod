package com.sonictms.alsys.wh.mapper;

import com.sonictms.alsys.wh.entity.WhZoneVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface WhZoneMapper {
    // 목록 조회
    List<WhZoneVO> selectZoneList(Map<String, Object> params);

    // 저장/수정
    void saveZone(WhZoneVO vo);

    // 삭제 (DELETED_YN = 'Y' 처리)
    void deleteZone(@Param("zoneId") Long zoneId, @Param("userId") String userId);
}