package com.allianceLogistics.alsys.erp.erp901006.mapper;

import com.allianceLogistics.alsys.erp.erp901006.entity.Erp901006VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp901006Mapper {

	
	//그리드 리스트 조회
	List<Erp901006VO> erp901006List( Erp901006VO erp901006VO);
	
	//양식 저장하기
	Erp901006VO erp901006p1Save( Erp901006VO erp90100VO);
	  	
	
	
	//수정팝업에서 내용 조회하기
	Erp901006VO erp901006p2List( Erp901006VO erp901006VO);
		
	
	
	/*		
	//배송확정일 가능한 날짜 조회
	List<Erp901006VO> erp901006p1List( Erp901006VO erp901006VO);
	
	
	//시공좌석 일괄생성 팝업창에서 일괄생성 헤더 저장하기
	Erp901006VO erp901006p1HeaderSave( Erp901006VO erp901006VO);	
	//시공좌석 일괄생성 팝업창에서 일괄생성 상세 저장하기
	Erp901006VO erp901006p1DetailSave( Erp901006VO erp901006VO);	
		
	//주문수정 팝업에서 헤더 불러오기		//헤더는 1줄만 불러옴
	Erp901006VO erp901006p3HeaderList( Erp901006VO erp901006VO);
	
	
	//주문수정 팝업에서 상세 불러오기		//상세는 리스트
	List<Erp901006VO> erp901006p3DetailList( Erp901006VO erp901006VO);
		
	//상세(제품정보) 삭제하기
	Erp901006VO erp901006p3DelDetail( Erp901006VO erp90100VO);		


	//시공좌석 일괄생성 팝업창에서 일괄생성 헤더 수정하기
	Erp901006VO erp901006p1HeaderUpdt( Erp901006VO erp901006VO);		
	
		
	

	//체크수정 하기 버튼
	Erp901006VO erp901006Save( Erp901006VO erp90100VO);		
			
	
	
	//erp901006p2 좌석 점유 상제 조회 리스트
	List<Erp901006VO> erp901006p2List( Erp901006VO erp90100VO);
	
	

	
	
	
	  //권역매핑삭제하기 
	Erp901006VO erp901006Delete( Erp901006VO erp90100VO);
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	Erp901006VO erp901006p1DoubleChk( Erp901006VO	  erp90100VO);
	  
	*/
	 
		

}