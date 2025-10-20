package com.sonictms.alsys.erp.erp104005.mapper;

import com.sonictms.alsys.erp.erp104005.entity.Erp104005VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp104005Mapper {

    // 그리드 리스트 조회
    List<Erp104005VO> erp104005List(Erp104005VO erp104005VO);
}
