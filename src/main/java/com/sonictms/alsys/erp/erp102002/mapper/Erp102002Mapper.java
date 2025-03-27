package com.sonictms.alsys.erp.erp102002.mapper;

import com.sonictms.alsys.erp.erp102002.entity.Erp102002VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp102002Mapper {


    //그리드 리스트 조회
    List<Erp102002VO> erp102002List(Erp102002VO erp90100VO);


    //팝업 등록/ 수정 하기
    Erp102002VO erp102002p1ComdSave(Erp102002VO erp90100VO);


    //고유 ID 로 정보 찾기
    Erp102002VO erp102002p1AlrmSrch(Erp102002VO erp90100VO);


    //알림톡 스케줄 실행 대상 조회
    List<Erp102002VO> erp102002ScdlExecLoad(Erp102002VO erp90100VO);


}