package com.sonictms.alsys.erp.erp104001.service;


import com.sonictms.alsys.erp.erp104001.entity.Erp104001VO;
import com.sonictms.alsys.erp.erp104001.mapper.Erp104001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104001Service {

    private final Erp104001Mapper erp104001Mapper;

    //그리드 불러오기
    public List<Erp104001VO> erp104001List(Erp104001VO erp104001VO) {
        List<Erp104001VO> list = erp104001Mapper.erp104001List(erp104001VO);
        return list;
    }
	
	/*
	
	//erp104001p2 좌석 점유 상제 조회 리스트
	public List<Erp104001VO> erp104001p2List(Erp104001VO erp104001VO) { 
		List<Erp104001VO> list =	  erp104001Mapper.erp104001p2List(erp104001VO); 
		return list; 
	}
	
		
		
		
	
	//시공좌석일괄생성 팝업창에서 일괄생성 저장하기
	public Erp104001VO erp104001p1Save(Erp104001VO erp104001VO) { 
		erp104001VO = erp104001Mapper.erp104001p1Save(erp104001VO);
		return erp104001VO; 
	}
	//체크수정 하기 버튼
	public Erp104001VO erp104001Save(Erp104001VO erp104001VO) { 
		erp104001VO	=	  erp104001Mapper.erp104001Save(erp104001VO); 
		return erp104001VO; 
	}

	  //권역매핑삭제하기 
	public Erp104001VO erp104001Delete(Erp104001VO erp104001VO) {
	  erp104001VO = erp104001Mapper.erp104001Delete(erp104001VO); return
	  erp104001VO; }
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	public Erp104001VO erp104001p1DoubleChk(Erp104001VO
	  erp104001VO) { erp104001VO =
	  erp104001Mapper.erp104001p1DoubleChk(erp104001VO); return erp104001VO; }
	*/


} 