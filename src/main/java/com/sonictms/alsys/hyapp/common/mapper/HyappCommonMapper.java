package com.sonictms.alsys.hyapp.common.mapper;

import com.sonictms.alsys.hyapp.common.entity.HyappCommonVO; // 패키지 경로 확인 필요
import org.apache.ibatis.annotations.Mapper;
import java.util.List;
import java.util.Map;

@Mapper
public interface HyappCommonMapper {

    // 물류센터 목록 조회 (운영 중인 센터만)
    List<Map<String, Object>> selectDcList(Map<String, Object> params);

}