package com.allianceLogistics.alsys.erp.erp104002.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.erp.erp104002.entity.Erp104002VO;


@Mapper
public interface Erp104002Mapper {

	
	//그리드 리스트 조회
	List<Erp104002VO> erp104002List( Erp104002VO erp90100VO);
	
	
	
	/*		
	 * 
	 * 
	 * 
	//시공좌석 일괄생성 팝업창에서 일괄생성 저장하기
	Erp104002VO erp104002p1Save( Erp104002VO erp90100VO);
	  
	//체크수정 하기 버튼
	Erp104002VO erp104002Save( Erp104002VO erp90100VO);		
			
	
	
	//erp104002p2 좌석 점유 상제 조회 리스트
	List<Erp104002VO> erp104002p2List( Erp104002VO erp90100VO);
	
	

	
	
	
	  //권역매핑삭제하기 
	Erp104002VO erp104002Delete( Erp104002VO erp90100VO);
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	Erp104002VO erp104002p1DoubleChk( Erp104002VO	  erp90100VO);
	  
	*/
	 
		

}