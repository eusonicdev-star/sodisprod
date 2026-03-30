package com.sonictms.alsys.erp.erp991002.mapper;


import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import com.sonictms.alsys.erp.erp991002.entity.Erp991002VO;


@Component
@Mapper
public interface Erp991002Mapper {

	
	//그리드 내용조회
	List<Erp991002VO> erp991002List( Erp991002VO erp90100VO);
	
	
	//그리드 수정내용 저장하기
	Erp991002VO erp991002Updt( Erp991002VO erp90100VO);
	
	//권한그룹 추가 팝업에서 간편 권한그룹 중복체크
	Erp991002VO erp991002p1DoubleChk( Erp991002VO erp90100VO);
	
	
	//권한그룹 간편등록
	Erp991002VO erp991002p1Save(Erp991002VO erp90100VO);
	
	
	
}