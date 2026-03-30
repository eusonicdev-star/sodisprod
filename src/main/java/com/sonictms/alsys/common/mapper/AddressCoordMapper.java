package com.sonictms.alsys.common.mapper; // 패키지명은 프로젝트 구조에 맞게 수정

import org.apache.ibatis.annotations.Mapper;
import java.util.Map;

@Mapper
public interface AddressCoordMapper {

    /**
     * 키로 좌표 조회
     * XML ID: selectCoordByKey
     */
    Map<String, Object> selectCoordByKey(String searchKey);

    /**
     * 좌표 정보 저장 (MERGE)
     * XML ID: insertCoord
     */
    int insertCoord(Map<String, Object> paramMap);
}