package com.sonictms.alsys.erp.erp902004.mapper;

import com.sonictms.alsys.erp.erp902004.entity.Erp902004VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp902004Mapper {


    //그리드 리스트 조회
    List<Erp902004VO> erp902004List(Erp902004VO erp90100VO);

    //물류센터매핑저장하기
    Erp902004VO erp902004Save(Erp902004VO erp90100VO);

    //물류센터매핑삭제하기
    Erp902004VO erp902004Delete(Erp902004VO erp90100VO);


}