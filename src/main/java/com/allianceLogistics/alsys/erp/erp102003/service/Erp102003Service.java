package com.allianceLogistics.alsys.erp.erp102003.service;

import com.allianceLogistics.alsys.erp.erp102003.entity.Erp102003VO;
import com.allianceLogistics.alsys.erp.erp102003.mapper.Erp102003Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp102003Service {

    private final Erp102003Mapper erp102003Mapper;

    //리스트조회 그리드 불러오기
    public List<Erp102003VO> erp102003List(Erp102003VO erp102003VO) {
        List<Erp102003VO> list = erp102003Mapper.erp102003List(erp102003VO);
        return list;
    }

/*	
	
	//팝업  등록/ 수정 하기
		public Erp102003VO erp102003p1ComdSave(Erp102003VO erp102003VO) { 
			erp102003VO =	  erp102003Mapper.erp102003p1ComdSave(erp102003VO); 
			return erp102003VO; 
		}

	//고유 ID 로 정보 찾기
	public Erp102003VO erp102003p1AlrmSrch(Erp102003VO erp102003VO) { 
		erp102003VO =	  erp102003Mapper.erp102003p1AlrmSrch(erp102003VO); 
		return erp102003VO; 
	}
	
	*/


} 