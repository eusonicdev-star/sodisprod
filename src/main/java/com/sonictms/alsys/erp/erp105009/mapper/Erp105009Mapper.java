package com.sonictms.alsys.erp.erp105009.mapper;

import com.sonictms.alsys.erp.erp105009.entity.Erp105009VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105009Mapper {

    List<Erp105009VO> erp105009List(Erp105009VO erp105009VO);
}


