package com.allianceLogistics.alsys.erp.erp104004.mapper;

import com.allianceLogistics.alsys.erp.erp104004.entity.Erp104004VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp104004Mapper {
    //그리드 리스트 조회
    List<Erp104004VO> erp104004List(Erp104004VO erp104004VO);

}