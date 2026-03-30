package com.sonictms.alsys.erp.erp105013.mapper;

import com.sonictms.alsys.erp.erp105013.entity.Erp105013VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp105013Mapper {

	
	//그리드 리스트 조회
	List<Erp105013VO> erp901007List(Erp105013VO erp105013VO);

	// 기존 임시 데이터 삭제
	void deleteStockTemp(Erp105013VO vo);

	// 신규 데이터 임시 저장
	void insertStockTemp(Erp105013VO vo);

	//임시 템플릿 리스트 조회
	List<Erp105013VO> getTempStockList(Erp105013VO erp105013VO);

	// 신규 데이터 임시 저장
	int insertMissingLocations(Erp105013VO vo);

	void insertFloorLocations(Erp105013VO vo);

	/**
	 * [최종반영 1단계] 기존 재고를 히스토리 테이블로 복사
	 */
	void insertStockHistory(Erp105013VO vo);

	/**
	 * [최종반영 2단계] 기존 재고 테이블 데이터 삭제
	 */
	void deleteActualStock(Erp105013VO vo);

	/**
	 * [최종반영 3단계] 임시 테이블 데이터를 실제 재고로 이관
	 * (로케이션 미지정 시 FLOOR_화주코드 자동 매핑 로직 포함)
	 */
	void insertFinalStock(Erp105013VO vo);

	void updateTempStatus(Erp105013VO vo);

	void updateMtrlMasterQuantity(Erp105013VO vo);
}