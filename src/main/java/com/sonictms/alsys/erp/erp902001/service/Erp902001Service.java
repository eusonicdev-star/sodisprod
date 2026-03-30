package com.sonictms.alsys.erp.erp902001.service;

import com.sonictms.alsys.erp.erp902001.entity.Erp902001VO;
import com.sonictms.alsys.erp.erp902001.mapper.Erp902001Mapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class Erp902001Service {

    private final Erp902001Mapper erp902001Mapper;

    //그리드 불러오기
    public List<Erp902001VO> erp902001List(Erp902001VO erp902001VO) {
        List<Erp902001VO> list = erp902001Mapper.erp902001List(erp902001VO);
        return list;
    }


    //지점코드 중복 확인
    public Erp902001VO erp902001p1CommDupl(Erp902001VO erp902001VO) {
        erp902001VO = erp902001Mapper.erp902001p1CommDupl(erp902001VO);
        return erp902001VO;
    }


    //지점(물류센터)고유 ID 로 정보 찾기
    public Erp902001VO erp902001p1comdDcSrch(Erp902001VO erp902001VO) {
        erp902001VO = erp902001Mapper.erp902001p1comdDcSrch(erp902001VO);
        return erp902001VO;
    }


    //팝업 지점 등록/ 수정 하기
    public Erp902001VO erp902001p1ComdSave(Erp902001VO erp902001VO) {
        erp902001VO = erp902001Mapper.erp902001p1ComdSave(erp902001VO);
        return erp902001VO;
    }
		
		
		
/*
	
	 
	//오른쪽 상세공통 불러오기
	public List<Erp991005VO> erp991005RComdList(Erp991005VO erp991005VO) { 
		List<Erp991005VO> list =	  erp991005Mapper.erp991005RComdList(erp991005VO); 
		return list; 
	}
	
	//등록/수정 팝업에서 수정일떄 내용 불러오기
	public Erp991005VO erp991005p1ComdSearch(Erp991005VO erp991005VO) { 
		erp991005VO= erp991005Mapper.erp991005p1ComdSearch(erp991005VO); 
		return erp991005VO; 
	}
	
	//등록/수정 팝업에서 저장하기
		public Erp991005VO erp991005p1ComdSave(Erp991005VO erp991005VO) { 
			erp991005VO= erp991005Mapper.erp991005p1ComdSave(erp991005VO); 
			return erp991005VO; 
		}
*/
} 