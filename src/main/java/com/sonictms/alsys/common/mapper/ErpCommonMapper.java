package com.sonictms.alsys.common.mapper;

import com.sonictms.alsys.common.entity.ErpCommonVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ErpCommonMapper {
	//20220517 정연호 kpp 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회하기. 1건 조회하기
	ErpCommonVO erpCommoSendValueSearch(ErpCommonVO erpCommonVO);

	//20220517 정연호 모바일 수행시 kpp 배송상태 변경을 위해 얼라이언스에서 보낼값을 조회하기. 1건 조회하기
	ErpCommonVO erpCommoSendValueSearchForMobile(ErpCommonVO erpCommonVO);
}