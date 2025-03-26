package com.allianceLogistics.alsys.common.mapper;

import com.allianceLogistics.alsys.common.entity.ErpCommonVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErpCommonMapper {
	//20220517 정연호 kpp 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회하기. 1건 조회하기
	ErpCommonVO erpCommoSendValueSearch(ErpCommonVO erpCommonVO);
	
	//20220517 정연호 모바일 수행시 kpp 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회하기. 1건 조회하기
	ErpCommonVO erpCommoSendValueSearchForMobile(ErpCommonVO erpCommonVO);
	
	
//////////////////////////////////////////	
	
	
	/******************8
	
	//kpp에서 불러온 데이터를 얼라이언스 테이블에 저장하는 곳
	ErpCommonVO outCmpySave(ErpCommonVO erpCommonVO);
	
	
	
	//데이터 체크
	ErpCommonVO erpCommonDataChk(ErpCommonVO erpCommonVO);
	
	//테이블에 저장된 외부업체(KPP)의 정보를 얼라이언스의 오더로 입력하기 - 화면의 그리드 한줄한줄을 넘겨받아 한번씩 한번씩 입력한다
	ErpCommonVO erpCommonAlOrdrSave(ErpCommonVO erpCommonVO);
	
	***********/
}