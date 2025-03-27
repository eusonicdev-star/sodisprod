package com.sonictms.alsys.erp.erp101004.mapper;

import com.sonictms.alsys.erp.erp101004.entity.Erp101004VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface Erp101004Mapper {


    //그리드 헤더 조회하기
    List<Erp101004VO> gridHeaderSrch101004(Erp101004VO erp101004VO);


    //엑셀데이터를 json으로 바꾼것을 체크하기
    Erp101004VO erp101004ExcelUploadCheck(Erp101004VO erp101004VO);


    //엑셀데이터를 json으로 바꾼것을 저장하기
    Erp101004VO erp101004ExcelUploadJson(Erp101004VO erp101004VO);


}