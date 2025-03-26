package com.allianceLogistics.alsys.mobile.mBarCode.mapper;

import com.allianceLogistics.alsys.mobile.mBarCode.entity.MobileBarCodeVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MobileBarCodeMapper {

	//모바일 바코드(판매오더번호) 로 상차조회하기 하기
	MobileBarCodeVO mBarCodeScan(MobileBarCodeVO mobileBarCodeVO);
	
	//모바일 바코드(판매오더번호) 로 상차변경하기
	MobileBarCodeVO mBarCodeLiftChange(MobileBarCodeVO mobileBarCodeVO);


	
}