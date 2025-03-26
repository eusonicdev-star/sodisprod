package com.allianceLogistics.alsys.erp.erp902001.mapper;

import com.allianceLogistics.alsys.erp.erp902001.entity.Erp902001VO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface Erp902001Mapper {


    //그리드 리스트 조회
    List<Erp902001VO> erp902001List(Erp902001VO erp90100VO);

    //중복체크
    Erp902001VO erp902001p1CommDupl(Erp902001VO erp90100VO);

    //지점(물류센터)고유 ID 로 정보 찾기
    Erp902001VO erp902001p1comdDcSrch(Erp902001VO erp90100VO);

    //팝업 지점 등록/ 수정 하기
    Erp902001VO erp902001p1ComdSave(Erp902001VO erp90100VO);
		
	
	/*
	
	
	//오른쪽 상세 공통코드 내용조회
	List<Erp991005VO> erp991005RComdList( Erp991005VO erp90100VO);
	
	//등록/수정 팝업에서 수정일떄 내용 불러오기
	Erp991005VO erp991005p1ComdSearch( Erp991005VO erp90100VO);
	
	//등록/수정 팝업에서 저장하기
	Erp991005VO erp991005p1ComdSave( Erp991005VO erp90100VO);
	*/
}