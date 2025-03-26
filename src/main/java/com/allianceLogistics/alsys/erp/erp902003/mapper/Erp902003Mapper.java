package com.allianceLogistics.alsys.erp.erp902003.mapper;

import com.allianceLogistics.alsys.erp.erp902003.entity.Erp902003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp902003Mapper {


    //그리드 리스트 조회
    List<Erp902003VO> erp902003List(Erp902003VO erp90100VO);

    //권역매핑저장하기
    Erp902003VO erp902003Save(Erp902003VO erp90100VO);

    //권역매핑삭제하기
    Erp902003VO erp902003Delete(Erp902003VO erp90100VO);

    //권역 등록 수정 팝업의 중복확인하기
    Erp902003VO erp902003p1DoubleChk(Erp902003VO erp90100VO);

    //권역 등록 수정 팝업의 등록/수정 하기
    Erp902003VO erp902003p1Save(Erp902003VO erp90100VO);


}