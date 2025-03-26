package com.allianceLogistics.alsys.erp.erp991005.mapper;

import com.allianceLogistics.alsys.erp.erp991005.entity.Erp991005VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp991005Mapper {


    //왼쪽 마스터 공통코드 내용조회
    List<Erp991005VO> erp991005LCommList(Erp991005VO erp90100VO);

    //오른쪽 상세 공통코드 내용조회
    List<Erp991005VO> erp991005RComdList(Erp991005VO erp90100VO);

    //등록/수정 팝업에서 수정일떄 내용 불러오기
    Erp991005VO erp991005p1ComdSearch(Erp991005VO erp90100VO);

    //등록/수정 팝업에서 저장하기
    Erp991005VO erp991005p1ComdSave(Erp991005VO erp90100VO);

}