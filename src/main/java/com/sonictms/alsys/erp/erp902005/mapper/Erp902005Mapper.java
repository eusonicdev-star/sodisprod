package com.sonictms.alsys.erp.erp902005.mapper;

import com.sonictms.alsys.erp.erp902005.entity.Erp902005VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp902005Mapper {


    //그리드 리스트 조회
    List<Erp902005VO> erp902005List(Erp902005VO erp90100VO);

    //일괄저장 하기 버튼
    Erp902005VO erp902005Save(Erp902005VO erp90100VO);

    //달력일괄생성 팝업창에서 일괄생성 저장하기
    Erp902005VO erp902005p1Save(Erp902005VO erp90100VO);
	/*	  
	  //권역매핑삭제하기 
	Erp902005VO erp902005Delete( Erp902005VO erp90100VO);
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	Erp902005VO erp902005p1DoubleChk( Erp902005VO	  erp90100VO);
	  
	*/


}