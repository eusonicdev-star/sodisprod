package com.sonictms.alsys.erp.erp203003.mapper;

import com.sonictms.alsys.erp.erp203003.entity.Erp203003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp203003Mapper {

    //메인화면 조회
    List<Erp203003VO> erp203003List(Erp203003VO erp203003VO);

    //응답률 조회
    List<Erp203003VO> erp203003List2(Erp203003VO erp203003VO);
}