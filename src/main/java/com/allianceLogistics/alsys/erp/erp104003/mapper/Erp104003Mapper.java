package com.allianceLogistics.alsys.erp.erp104003.mapper;

import com.allianceLogistics.alsys.erp.erp104003.entity.Erp104003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp104003Mapper {


    //그리드 리스트 조회
    List<Erp104003VO> erp104003List(Erp104003VO erp90100VO);
	
	
	
	/*		
	 * 
	 * 
	 * 
	//시공좌석 일괄생성 팝업창에서 일괄생성 저장하기
	Erp104003VO erp104003p1Save( Erp104003VO erp90100VO);
	  
	//체크수정 하기 버튼
	Erp104003VO erp104003Save( Erp104003VO erp90100VO);		
			
	
	
	//erp104003p2 좌석 점유 상제 조회 리스트
	List<Erp104003VO> erp104003p2List( Erp104003VO erp90100VO);
	
	

	
	
	
	  //권역매핑삭제하기 
	Erp104003VO erp104003Delete( Erp104003VO erp90100VO);
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	Erp104003VO erp104003p1DoubleChk( Erp104003VO	  erp90100VO);
	  
	*/


}