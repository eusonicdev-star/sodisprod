package com.allianceLogistics.alsys.erp.erp203002.mapper;


import com.allianceLogistics.alsys.erp.erp203002.entity.Erp203002VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp203002Mapper {

	//그리드 리스트 조회
	List<Erp203002VO> erp203002List( Erp203002VO erp203002VO);	
	
	
	//체크한것들 확정/취소
	Erp203002VO erp203002rowCnfmCnsl( Erp203002VO erp90100VO);		

	
	
	//모바일확정 일괄 확정/취소 팝업에서 일괄 확정/취소 하기
	Erp203002VO erp203002allCnfmCnsl( Erp203002VO erp90100VO);		
	
	
	/*	
	
	//체크한것들 삭제(일괄삭제)
	Erp203002VO erp203002ChkDel( Erp203002VO erp90100VO);		
				

	
	
	
	//배송확정일 가능한 날짜 조회
	List<Erp203002VO> erp203002p1List( Erp203002VO erp203002VO);
	
	
	//시공좌석 일괄생성 팝업창에서 일괄생성 헤더 저장하기
	Erp203002VO erp203002p1HeaderSave( Erp203002VO erp203002VO);	
	//시공좌석 일괄생성 팝업창에서 일괄생성 상세 저장하기
	Erp203002VO erp203002p1DetailSave( Erp203002VO erp203002VO);	
		
	//주문수정 팝업에서 헤더 불러오기		//헤더는 1줄만 불러옴
	Erp203002VO erp203002p3HeaderList( Erp203002VO erp203002VO);
	
	
	//주문수정 팝업에서 상세 불러오기		//상세는 리스트
	List<Erp203002VO> erp203002p3DetailList( Erp203002VO erp203002VO);
		
	//상세(제품정보) 삭제하기
	Erp203002VO erp203002p3DelDetail( Erp203002VO erp90100VO);		


	//시공좌석 일괄생성 팝업창에서 일괄생성 헤더 수정하기
	Erp203002VO erp203002p1HeaderUpdt( Erp203002VO erp203002VO);		
	
	

	//시공좌석 일괄생성 팝업창에서 일괄생성 저장하기
	Erp203002VO erp203002p1Save( Erp203002VO erp90100VO);
	  
	
	
	
	//erp203002p2 좌석 점유 상제 조회 리스트
	List<Erp203002VO> erp203002p2List( Erp203002VO erp90100VO);
	
	

	
	
	
	  //권역매핑삭제하기 
	Erp203002VO erp203002Delete( Erp203002VO erp90100VO);
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	Erp203002VO erp203002p1DoubleChk( Erp203002VO	  erp90100VO);
	  
	*/
	 
		

}