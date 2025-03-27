package com.sonictms.alsys.erp.erp902005.service;

import com.sonictms.alsys.erp.erp902005.entity.Erp902005VO;
import com.sonictms.alsys.erp.erp902005.mapper.Erp902005Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp902005Service {

    private final Erp902005Mapper erp902005Mapper;

    //그리드 불러오기
    public List<Erp902005VO> erp902005List(Erp902005VO erp902005VO) {
        List<Erp902005VO> list = erp902005Mapper.erp902005List(erp902005VO);
        return list;
    }

    //일괄저장 하기 버튼
    public Erp902005VO erp902005Save(Erp902005VO erp902005VO) {
        erp902005VO = erp902005Mapper.erp902005Save(erp902005VO);
        return erp902005VO;
    }

    //달력일괄생성 팝업창에서 일괄생성 저장하기
    public Erp902005VO erp902005p1Save(Erp902005VO erp902005VO) {
        erp902005VO = erp902005Mapper.erp902005p1Save(erp902005VO);
        return erp902005VO;
    }
		
	/*
	  //권역매핑삭제하기 
	public Erp902005VO erp902005Delete(Erp902005VO erp902005VO) {
	  erp902005VO = erp902005Mapper.erp902005Delete(erp902005VO); return
	  erp902005VO; }
	  
	  //권역 등록 수정 팝업의 중복확인하기 
	public Erp902005VO erp902005p1DoubleChk(Erp902005VO
	  erp902005VO) { erp902005VO =
	  erp902005Mapper.erp902005p1DoubleChk(erp902005VO); return erp902005VO; }
	*/


} 