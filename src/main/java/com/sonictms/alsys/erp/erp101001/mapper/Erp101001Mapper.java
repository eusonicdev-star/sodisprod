package com.sonictms.alsys.erp.erp101001.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.sonictms.alsys.erp.erp101001.entity.Erp101001VO;

@Mapper
public interface Erp101001Mapper {

	// 그리드 리스트 조회
	List<Erp101001VO> erp101001List(Erp101001VO erp101001VO);

	// 배송확정일 가능한 날짜 조회
	List<Erp101001VO> erp101001p1List(Erp101001VO erp101001VO);

	// 시공좌석 일괄생성 팝업창에서 일괄생성 헤더 저장하기
	Erp101001VO erp101001p1HeaderSave(Erp101001VO erp101001VO);

	// 시공좌석 일괄생성 팝업창에서 일괄생성 상세 저장하기
	Erp101001VO erp101001p1DetailSave(Erp101001VO erp101001VO);

	// 주문수정 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
	Erp101001VO erp101001p3HeaderList(Erp101001VO erp101001VO);

	// 주문수정 팝업에서 상세 불러오기 //상세는 리스트
	List<Erp101001VO> erp101001p3DetailList(Erp101001VO erp101001VO);

	// 상세(제품정보) 삭제하기
	Erp101001VO erp101001p3DelDetail(Erp101001VO erp90100VO);
	
	//20211226 정연호 추가. 상세(제품정보) 그리드의 고객사주문번호와 착불비 수정하기
	Erp101001VO erp101001p3UpdtDetail(Erp101001VO erp90100VO);
		
		

	// 시공좌석 일괄생성 팝업창에서 일괄생성 헤더 수정하기
	Erp101001VO erp101001p1HeaderUpdt(Erp101001VO erp101001VO);

	// 원오더찾기 팝업에서 리스트조회
	List<Erp101001VO> erp101001p4List(Erp101001VO erp101001VO);

	// 반품등록 팝업에서 헤더 불러오기 //헤더는 1줄만 불러옴
	Erp101001VO erp101001p5HeaderList(Erp101001VO erp101001VO);

	// 부품등록 팝업에서 상세 불러오기 //상세는 리스트
	List<Erp101001VO> erp101001p5DetailList(Erp101001VO erp101001VO);

	// 반품등록 헤더 저장하기
	Erp101001VO erp101001p5HeaderSave(Erp101001VO erp101001VO);

	// 반품등록 상세 저장하기
	Erp101001VO erp101001p5DetailSave(Erp101001VO erp101001VO);

	/// 우편번호로 물류센터 찾기
	Erp101001VO erp101001p1PostCdDcCd(Erp101001VO erp101001VO);

	// 체크한 것들 삭제(일괄삭제)
	Erp101001VO erp101001ChkDel(Erp101001VO erp90100VO);
}