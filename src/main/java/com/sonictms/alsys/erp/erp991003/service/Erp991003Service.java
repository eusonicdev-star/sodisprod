package com.sonictms.alsys.erp.erp991003.service;

import com.sonictms.alsys.erp.erp991003.entity.Erp991003VO;
import com.sonictms.alsys.erp.erp991003.mapper.Erp991003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp991003Service {

    private final Erp991003Mapper erp991003Mapper;

    //사용자 목록 그리드 리스트 불러오기
    public List<Erp991003VO> erp991003List(Erp991003VO erp991003VO) {
        List<Erp991003VO> list = erp991003Mapper.erp991003List(erp991003VO);
        return list;
    }
	 
	/*
	//메뉴 권한관리 수정내용 저장하기
	public Erp991003VO erp991003Updt(List<Erp991003VO> list) { 
		Erp991003VO erp991003VO = null;
		for (int i=0;i<list.size();i++)
		{
			erp991003VO =	  erp991003Mapper.erp991003Updt(list.get(i));
		}
		return erp991003VO; 
	}
	  */

    //회사코드와 사용자 아이디로 아이디 중복을 체크
    public Erp991003VO erp991003p1DoubleChk(Erp991003VO erp991003VO) {
        erp991003VO = erp991003Mapper.erp991003p1DoubleChk(erp991003VO);
        return erp991003VO;
    }


    //tblUserMId 로 사용자 정보 불러오기
    public Erp991003VO erp991003p1TblUserMId(Erp991003VO erp991003VO) {
        erp991003VO = erp991003Mapper.erp991003p1TblUserMId(erp991003VO);
        return erp991003VO;
    }


    //사용자 등록/수정
    public Erp991003VO erp991003p1ComdSave(Erp991003VO erp991003VO) {

        erp991003VO = erp991003Mapper.erp991003p1ComdSave(erp991003VO);

        return erp991003VO;
    }


    //비밀번호 초기화 - 비밀번호를 아이디와 똑같게 만듦
    public Erp991003VO erp991003p1PwReset(Erp991003VO erp991003VO) {
        erp991003VO = erp991003Mapper.erp991003p1PwReset(erp991003VO);
        return erp991003VO;
    }


} 