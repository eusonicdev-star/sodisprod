package com.sonictms.alsys.erp.erp105001.mapper;

import com.sonictms.alsys.erp.erp105001.entity.Erp105001VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105001Mapper {

    // 재고 현황 리스트 조회
    List<Erp105001VO> erp105001List(Erp105001VO erp105001VO);

    // 재고 조정
    int adjustInventory(Erp105001VO erp105001VO);

}
