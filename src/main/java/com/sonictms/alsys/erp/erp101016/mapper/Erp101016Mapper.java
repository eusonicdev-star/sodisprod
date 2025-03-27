package com.sonictms.alsys.erp.erp101016.mapper;

import com.sonictms.alsys.erp.erp101016.entity.Erp101016VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp101016Mapper {

	// 그리드 리스트 조회
	List<Erp101016VO> erp101016List(Erp101016VO erp101016VO);

	// 조치이력 상세보기 팝업의 그리드 리스트 조회하기
	List<Erp101016VO> erp101016p3List(Erp101016VO erp101016VO);
	
	// 취할 조치 없음 팝업의 예 또는 아니오 버튼을 눌러 값을 저장할때 수행함
	Erp101016VO erp101016p6Save(Erp101016VO erp101016VO);
	
	// 반품등록 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
	Erp101016VO erp101016p1HeaderList(Erp101016VO erp101016VO);
	
	// 부품등록 팝업에서 상세 불러오기 //상세는 리스트
	List<Erp101016VO> erp101016p1DetailList(Erp101016VO erp101016VO);
	
	// 미마감 주문 등록 헤더 저장하기
	Erp101016VO erp101016p1HeaderSave(Erp101016VO erp101016VO);	
	
	// 미마감 주문 등록 상 저장하기
	Erp101016VO erp101016p1MissSave(Erp101016VO erp101016VO);	
	
	// 미마감 주문 등록 상세 저장하기
	Erp101016VO erp101016p1DetailSave(Erp101016VO erp101016VO);
	
	/// 우편번호로 물류센터 찾기
	Erp101016VO erp101016p1PostCdDcCd(Erp101016VO Erp101016VO);	
	
	// 배송확정일 가능한 날짜 조회
	List<Erp101016VO> erp101016p1List(Erp101016VO erp101016VO);
	
	// 원오더찾기 팝업에서 리스트조회
	List<Erp101016VO> erp101016p0List(Erp101016VO erp101016VO);	

}