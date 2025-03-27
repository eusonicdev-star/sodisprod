package com.sonictms.alsys.erp.erp901002.mapper;

import com.sonictms.alsys.erp.erp901002.entity.Erp901002ExcelVO;
import com.sonictms.alsys.erp.erp901002.entity.Erp901002VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp901002Mapper {


    //그리드 리스트 조회
    List<Erp901002VO> erp901002List(Erp901002VO erp90100VO);

    //팝업 등록/ 수정 하기
    Erp901002VO erp901002p1ComdSave(Erp901002VO erp90100VO);


    //고유 ID 로 정보 찾기
    Erp901002VO erp901002p1mtrlSrch(Erp901002VO erp90100VO);


    //엑셀데이터를 json으로 바꾼것을 저장하기
    Erp901002ExcelVO erp901002p2ExcelUploadJson(Erp901002ExcelVO erp901002ExcelVO);


}