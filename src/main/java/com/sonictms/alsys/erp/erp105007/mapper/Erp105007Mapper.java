package com.sonictms.alsys.erp.erp105007.mapper;

import com.sonictms.alsys.erp.erp105007.entity.Erp105007VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105007Mapper {

    List<Erp105007VO> erp105007List(Erp105007VO erp105007VO);
}
