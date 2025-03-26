package com.allianceLogistics.alsys.erp.erp102003.mapper;


import com.allianceLogistics.alsys.erp.erp102003.entity.Erp102003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface Erp102003Mapper {


    //그리드 리스트 조회
    List<Erp102003VO> erp102003List(Erp102003VO erp90100VO);
	/*
	//팝업 등록/ 수정 하기
	Erp102003VO erp102003p1ComdSave( Erp102003VO erp90100VO);

	//고유 ID 로 정보 찾기
	Erp102003VO erp102003p1AlrmSrch( Erp102003VO erp90100VO);		
*/


}