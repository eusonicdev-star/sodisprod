package com.allianceLogistics.alsys.erp.erp202001.mapper;

import com.allianceLogistics.alsys.erp.erp202001.entity.Erp202001VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp202001Mapper {


    //그리드 리스트 조회
    List<Erp202001VO> erp202001List(Erp202001VO erp90100VO);

    //시공좌석 일괄생성 팝업창에서 일괄생성 저장하기
    Erp202001VO erp202001p1Save(Erp202001VO erp90100VO);

    //체크수정 하기 버튼
    Erp202001VO erp202001Save(Erp202001VO erp90100VO);


    //erp202001p2 좌석 점유 상제 조회 리스트
    List<Erp202001VO> erp202001p2List(Erp202001VO erp90100VO);


    //좌석마감 하기 버튼
    Erp202001VO erp202001End(Erp202001VO erp90100VO);


}