package com.sonictms.alsys.erp.erp104003.mapper;

import com.sonictms.alsys.erp.erp104003.entity.Erp104003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp104003Mapper {
    //그리드 리스트 조회
    List<Erp104003VO> erp104003List(Erp104003VO erp90100VO);
}
