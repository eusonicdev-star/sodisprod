package com.sonictms.alsys.erp.erp104002.service;


import com.sonictms.alsys.erp.erp104002.entity.Erp104002VO;
import com.sonictms.alsys.erp.erp104002.mapper.Erp104002Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp104002Service {

    private final Erp104002Mapper erp104002Mapper;

    //그리드 불러오기
    public List<Erp104002VO> erp104002List(Erp104002VO erp104002VO) {
        List<Erp104002VO> list = erp104002Mapper.erp104002List(erp104002VO);
        return list;
    }
	
	/*
	
	//erp104002p2 좌석 점유 상제 조회 리스트
	public List<Erp104002VO> erp104002p2List(Erp104002VO erp104002VO) { 
		List<Erp104002VO> list =	  erp104002Mapper.erp104002p2List(erp104002VO); 
		return list; 
	}
	
		
		
		
	
	//시공좌석일괄생성 팝업창에서 일괄생성 저장하기
	public Erp104002VO erp104002p1Save(Erp104002VO erp104002VO) { 
		erp104002VO = erp104002Mapper.erp104002p1Save(erp104002VO);
		return erp104002VO; 
	}
	//체크수정 하기 버튼
	public Erp104002VO erp104002Save(Erp104002VO erp104002VO) { 
		erp104002VO	=	  erp104002Mapper.erp104002Save(erp104002VO); 
		return erp104002VO; 
	}
	
		

	

		

	  //권역매핑삭제하기 
	public Erp104002VO erp104002Delete(Erp104002VO erp104002VO) {
	  erp104002VO = erp104002Mapper.erp104002Delete(erp104002VO); return
	  erp104002VO; }
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	public Erp104002VO erp104002p1DoubleChk(Erp104002VO
	  erp104002VO) { erp104002VO =
	  erp104002Mapper.erp104002p1DoubleChk(erp104002VO); return erp104002VO; }
	*/


} 