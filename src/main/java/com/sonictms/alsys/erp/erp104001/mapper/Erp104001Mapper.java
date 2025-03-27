package com.sonictms.alsys.erp.erp104001.mapper;


import com.sonictms.alsys.erp.erp104001.entity.Erp104001VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface Erp104001Mapper {


    //그리드 리스트 조회
    List<Erp104001VO> erp104001List(Erp104001VO erp90100VO);
	
	/*
	//시공좌석 일괄생성 팝업창에서 일괄생성 저장하기
	Erp104001VO erp104001p1Save( Erp104001VO erp90100VO);
	  
	//체크수정 하기 버튼
	Erp104001VO erp104001Save( Erp104001VO erp90100VO);		

	//erp104001p2 좌석 점유 상제 조회 리스트
	List<Erp104001VO> erp104001p2List( Erp104001VO erp90100VO);

	
	  //권역매핑삭제하기 
	Erp104001VO erp104001Delete( Erp104001VO erp90100VO);
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	Erp104001VO erp104001p1DoubleChk( Erp104001VO	  erp90100VO);
	  
	*/


}