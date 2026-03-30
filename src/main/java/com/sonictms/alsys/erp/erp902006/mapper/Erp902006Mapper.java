package com.sonictms.alsys.erp.erp902006.mapper;

import com.sonictms.alsys.erp.erp902006.entity.Erp902006VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp902006Mapper {


    //그리드 리스트 조회
    List<Erp902006VO> erp902006List(Erp902006VO erp90100VO);

    //일괄저장 하기 버튼
    Erp902006VO erp902006Save(Erp902006VO erp90100VO);

	/*
	//달력일괄생성 팝업창에서 일괄생성 저장하기
		Erp902006VO erp902006p1Save( Erp902006VO erp90100VO);
	*/


}