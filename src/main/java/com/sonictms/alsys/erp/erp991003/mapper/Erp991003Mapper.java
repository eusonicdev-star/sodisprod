package com.sonictms.alsys.erp.erp991003.mapper;

import com.sonictms.alsys.erp.erp991003.entity.Erp991003VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp991003Mapper {


    //사용자 목록 그리드 내용조회
    List<Erp991003VO> erp991003List(Erp991003VO erp90100VO);
	
	/*
	//그리드 수정내용 저장하기
	Erp991003VO erp991003Updt( Erp991003VO erp90100VO);
	*/

    //회사코드와 사용자 아이디로 아이디 중복을 체크
    Erp991003VO erp991003p1DoubleChk(Erp991003VO erp90100VO);


    //tblUserMId 로 사용자 정보 불러오기
    Erp991003VO erp991003p1TblUserMId(Erp991003VO erp90100VO);


    /// /사용자 등록/수정
    Erp991003VO erp991003p1ComdSave(Erp991003VO erp90100VO);


    //비밀번호 초기화 - 비밀번호를 아이디와 똑같게 만듦
    Erp991003VO erp991003p1PwReset(Erp991003VO erp90100VO);
}