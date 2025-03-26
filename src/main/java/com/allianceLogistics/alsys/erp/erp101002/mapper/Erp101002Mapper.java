package com.allianceLogistics.alsys.erp.erp101002.mapper;

import com.allianceLogistics.alsys.erp.erp101002.entity.Erp101002VO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface Erp101002Mapper {
	

	//그리드 헤더 조회하기
	List<Erp101002VO> gridHeaderSrch( Erp101002VO erp101002VO);

	
	//엑셀데이터를 json으로 바꾼것을 체크하기
		Erp101002VO erp101002ExcelUploadCheck( Erp101002VO erp101002VO);	
		
		
	//엑셀데이터를 json으로 바꾼것을 저장하기
	Erp101002VO erp101002ExcelUploadJson( Erp101002VO erp101002VO);	
	
	

}