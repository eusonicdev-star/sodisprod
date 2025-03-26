package com.allianceLogistics.alsys.erp.erp991004.mapper;

import com.allianceLogistics.alsys.erp.erp991004.entity.Erp991004VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp991004Mapper {


    //그리드 내용조회
    List<Erp991004VO> erp991004List(Erp991004VO erp991004VO);


}