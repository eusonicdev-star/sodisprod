package com.sonictms.alsys.erp.erp101003.mapper;

import com.sonictms.alsys.erp.erp101003.entity.Erp101003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp101003Mapper {

	// 그리드 리스트 조회
	List<Erp101003VO> erp101003List(Erp101003VO erp101003VO);

	// 체크한 것들 삭제(일괄삭제)
	Erp101003VO erp101003ChkDel(Erp101003VO erp90100VO);

	// 체크한 것들 변경(일괄변경)
	Erp101003VO erp101003chkUpdt(Erp101003VO erp90100VO);
	
	
	//20211226 정연호 추가.  체크한 것들 주문복사
	Erp101003VO erp101003chkCopy(Erp101003VO erp90100VO);
}