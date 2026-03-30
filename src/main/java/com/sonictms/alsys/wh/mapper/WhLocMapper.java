package com.sonictms.alsys.wh.mapper;

import com.sonictms.alsys.wh.entity.WhLocVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface WhLocMapper {
    /**
     * 로케이션 목록 조회
     */
    List<WhLocVO> selectLocList(Map<String, Object> params);

    /**
     * 로케이션 저장 및 수정 (MERGE)
     */
    void saveLocation(WhLocVO vo);

    /**
     * 로케이션 삭제 (Soft Delete)
     */
    void deleteLocation(Map<String, Object> params);
}