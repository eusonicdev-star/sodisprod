package com.sonictms.alsys.erp.erp102003.mapper;

import com.sonictms.alsys.erp.erp102003.entity.Erp102003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp102003Mapper {
    List<Erp102003VO> erp102003List(Erp102003VO erp90100VO);
}
