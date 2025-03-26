package com.allianceLogistics.alsys.erp.erp104003.service;


import com.allianceLogistics.alsys.erp.erp104003.entity.Erp104003VO;
import com.allianceLogistics.alsys.erp.erp104003.mapper.Erp104003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104003Service {

    private final Erp104003Mapper erp104003Mapper;

    //그리드 불러오기
    public List<Erp104003VO> erp104003List(Erp104003VO erp104003VO) {
        List<Erp104003VO> list = erp104003Mapper.erp104003List(erp104003VO);
        return list;
    }
	
	/*
	
	//erp104003p2 좌석 점유 상제 조회 리스트
	public List<Erp104003VO> erp104003p2List(Erp104003VO erp104003VO) { 
		List<Erp104003VO> list =	  erp104003Mapper.erp104003p2List(erp104003VO); 
		return list; 
	}
	
		
		
		
	
	//시공좌석일괄생성 팝업창에서 일괄생성 저장하기
	public Erp104003VO erp104003p1Save(Erp104003VO erp104003VO) { 
		erp104003VO = erp104003Mapper.erp104003p1Save(erp104003VO);
		return erp104003VO; 
	}
	//체크수정 하기 버튼
	public Erp104003VO erp104003Save(Erp104003VO erp104003VO) { 
		erp104003VO	=	  erp104003Mapper.erp104003Save(erp104003VO); 
		return erp104003VO; 
	}
	
		

	

		

	  //권역매핑삭제하기 
	public Erp104003VO erp104003Delete(Erp104003VO erp104003VO) {
	  erp104003VO = erp104003Mapper.erp104003Delete(erp104003VO); return
	  erp104003VO; }
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	public Erp104003VO erp104003p1DoubleChk(Erp104003VO
	  erp104003VO) { erp104003VO =
	  erp104003Mapper.erp104003p1DoubleChk(erp104003VO); return erp104003VO; }
	*/


} 