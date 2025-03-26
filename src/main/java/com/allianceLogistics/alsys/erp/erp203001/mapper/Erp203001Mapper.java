package com.allianceLogistics.alsys.erp.erp203001.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.allianceLogistics.alsys.erp.erp203001.entity.Erp203001VO;

@Mapper
public interface Erp203001Mapper {

	// 그리드 리스트 조회
	List<Erp203001VO> erp203001List(Erp203001VO erp203001VO);

	// 리스트조회2 - 아래쪽 상세 그리드 조회
	List<Erp203001VO> erp203001List2(Erp203001VO erp203001VO);

	// 시공기사 찾기 조회
	List<Erp203001VO> erp203001InstErSrch(Erp203001VO erp203001VO);

	// 시공기사 찾기 조회2
	List<Erp203001VO> erp203001InstErSrch2(Erp203001VO erp203001VO);

	// 시공기사 현황 팝업3
	List<Erp203001VO> erp203001InstErSrch3(Erp203001VO erp203001VO);

	// 그리드에서 순번, 팔렛트, 시공기사 입력한것 저장 또는 수정 하기
	Erp203001VO erp203001Save(Erp203001VO erp203001VO);

	// 체크한것들 삭제(일괄삭제)
	Erp203001VO erp203001DelChk(Erp203001VO erp203001VO);

}