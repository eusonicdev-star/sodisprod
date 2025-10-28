package com.sonictms.alsys.erp.erp105008.mapper;

import com.sonictms.alsys.erp.erp105008.entity.Erp105008VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105008Mapper {

    List<Erp105008VO> erp105008List(Erp105008VO erp105008VO);
}
