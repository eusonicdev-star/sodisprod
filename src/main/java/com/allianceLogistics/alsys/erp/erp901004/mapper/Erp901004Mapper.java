package com.allianceLogistics.alsys.erp.erp901004.mapper;

import com.allianceLogistics.alsys.erp.erp901004.entity.Erp901004ExcelVO;
import com.allianceLogistics.alsys.erp.erp901004.entity.Erp901004VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp901004Mapper {


    //그리드 리스트 조회
    List<Erp901004VO> erp901004List(Erp901004VO erp90100VO);

    //팝업 왼쪽 하위 품목 찾기
    List<Erp901004VO> erp901004p1ComdSrch(Erp901004VO erp90100VO);


    //팝업 등록/ 수정 하기
    Erp901004VO erp901004p1ComdSave(Erp901004VO erp90100VO);


    //정보 찾기
    List<Erp901004VO> erp901004prntMtrlList(Erp901004VO erp90100VO);

    //로우 삭제

    //Erp901004VO erp901004p1DelTblBomId( Erp901004VO erp90100VO);

    //엑셀데이터를 json으로 바꾼것을 저장하기
    Erp901004ExcelVO erp901004p2ExcelUploadJson(Erp901004ExcelVO erp901004ExcelVO);

}